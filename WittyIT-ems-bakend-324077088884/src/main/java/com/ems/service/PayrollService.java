package com.ems.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Employee;
import com.ems.domain.PaySlip;
import com.ems.domain.PayrollFixed;
import com.ems.domain.PayrollVariable;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.repository.PaySlipRepository;
import com.ems.repository.PayrollFixedRepository;
import com.ems.repository.PayrollVariableRepository;
import com.ems.servicefinder.utils.PaySlipComparator;
import com.ems.servicefinder.utils.PaySlipTags;
import com.ems.wrappers.FileUploadWrapper;
import com.ems.wrappers.PaySlipWrapper;

@Service
@Transactional
public class PayrollService {

	private static final Integer TAGS_ROW = 0;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PaySlipRepository paySlipRepository;

	@Autowired
	private PayrollFixedRepository payrollFixedRepository;

	@Autowired
	private PayrollVariableRepository payrollVariableRepository;

	/**
	 * To process uploaded Pay Slip excel
	 * 
	 * Url associated: /payroll/pay/upload
	 * 
	 * @param uploadWrapper
	 *            FileUploadWrapper
	 * @throws ServiceException
	 */
	@SuppressWarnings("resource")
	@Transactional(rollbackFor = ServiceException.class)
	public void processPaySlipSheet(FileUploadWrapper uploadWrapper, Integer month, Integer year)
			throws ServiceException {
		// Integer month = uploadWrapper.getMonth();
		// Integer year = uploadWrapper.getYear();
		if (month > 12 || month < 1)
			throw new ServiceException("Invalid month.");

		DateTime today = new DateTime();

		if ((month > today.getMonthOfYear() && year >= today.getYear()) || year > today.getYear())
			throw new ServiceException("Future month and year not allowed");

		Workbook workbook;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(uploadWrapper.getFileContent().getBytes());

			if (uploadWrapper.getFileContent().getOriginalFilename().endsWith("xls")) {
				workbook = new HSSFWorkbook(bis);
			} else if (uploadWrapper.getFileContent().getOriginalFilename().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(bis);
			} else {
				throw new ServiceException("Received file does not have a standard excel extension.");
			}

			List<String> tags = new ArrayList<>();

			// DateTime date =
			// DateTime.parse(workbook.getSheetAt(0).getSheetName(),
			// DateTimeFormat.forPattern("MMMM-yyyy"));

			DateTime date = new DateTime().withMonthOfYear(month).withYear(year);

			for (Row row : workbook.getSheetAt(0)) {
				Iterator<Cell> cellIterator = row.cellIterator();

				if (row.getRowNum() == TAGS_ROW) {
					while (cellIterator.hasNext())
						tags.add(cellIterator.next().getStringCellValue());
				} else if (row.getRowNum() > TAGS_ROW) {
					Employee employee = null;

					Cell empCodeCell = row.getCell(tags.indexOf("Employee Code"));
					
					Cell error1 = row.getCell(tags.indexOf("No. of month days"));
					Cell error2 = row.getCell(tags.indexOf("No. of working days"));
					try {
						error1.setCellType(Cell.CELL_TYPE_NUMERIC);
						error2.setCellType(Cell.CELL_TYPE_NUMERIC);
					} catch (Exception e1) {
					}

					if (empCodeCell != null) {
						empCodeCell.setCellType(Cell.CELL_TYPE_STRING);
						
						
						
						if(empCodeCell.getStringCellValue().isEmpty() && (error1 != null && error1.getNumericCellValue() != 0.0) && (error2 != null && error2.getNumericCellValue() != 0.0))
							throw new ServiceException("Employee code is mandatory. Row No :- " + (row.getRowNum() + 1));
						employee = employeeService.findByEmpCode(empCodeCell.getStringCellValue());
						
						if(employee != null && employee.getId().longValue() == 1l)
							continue;
						
						if (employee != null && !((error1 != null && error1.getNumericCellValue() != 0.0) && (error2 != null && error2.getNumericCellValue() != 0.0)))
							throw new ServiceException("'No. of working days' and 'No. of month days' should be given. Row No :- " + (row.getRowNum() + 1));
							
					}else {
						if((error1 != null && error1.getNumericCellValue() != 0.0) && (error2 != null && error2.getNumericCellValue() != 0.0))
							throw new ServiceException("Employee code is mandatory. Row No :- " + (row.getRowNum() + 1));
						else {
							continue;
						}
					}

					if (employee != null) {
						PaySlip paySlip = paySlipRepository.findByEmpIdAndMonthAndYear(employee.getId(),
								date.getMonthOfYear(), date.getYear());

						if (paySlip == null)
							paySlip = new PaySlip(employee.getId(), date.getMonthOfYear(), date.getYear());

						paySlip.setEmpCode(employee.getEmpCode());
						paySlip.setEmpName(employee.getFullName());

						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();

							try {
								Double value = cell.getNumericCellValue();
								if (value != null)
									addPaySlipEntry(paySlip, tags.get(cell.getColumnIndex()), value.floatValue());
							} catch (IllegalStateException | NumberFormatException e) {
							}
						}
						paySlipRepository.save(paySlip);
					}else {
						throw new ServiceException("The employee code is invalid. Row No :- " + (row.getRowNum() + 1));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("Unable to upload payslip: " + e.getMessage());
		}
	}

	/**
	 * Sets the data to pay slip.
	 * 
	 * @param paySlip
	 * @param tag
	 * @param value
	 *            the tag value
	 * @throws ServiceException
	 * @throws SerialException
	 */
	private void addPaySlipEntry(PaySlip paySlip, String tag, Float value) throws ServiceException, SerialException {
		PaySlipTags tagName = PaySlipTags.getEnum(tag);
		if (tagName == null) {
			return;
		}
		switch (tagName) {
		case WORKING_DAYS:
			DateTime paySlipDate = new DateTime().withDayOfMonth(1).withMonthOfYear(paySlip.getMonth())
					.withYear(paySlip.getYear());

			if (paySlipDate.dayOfMonth().getMaximumValue() < value)
				throw new ServiceException("Incorrect working days for " + paySlip.getEmpName() + " with code "
						+ paySlip.getEmpCode() + " .");

			paySlip.setWorkingDays(value);
			break;

		case BASIC_SALARY:
			paySlip.setBasicSalary(value);
			break;

		case HRA:
			paySlip.setHra(value);
			break;

		case MEDICAL:
			paySlip.setMedical(value);
			break;

		case SPECIAL_ALLOWANCES:
			paySlip.setSpecialAllowances(value);
			break;

		case LTA:
			paySlip.setLta(value);
			break;

		case ARREAR:
			paySlip.setArrear(value);
			break;

		case VEHICLE_RUNNING:
			paySlip.setVehicleRunning(value);
			break;

		case ATTIRE_ALLOWANCES:
			paySlip.setAttireAllowances(value);
			break;

		case VEHICLE_REIMBURSE:
			paySlip.setVehicleReimburse(value);
			break;

		case TELEPHONE_REIMBURSE:
			paySlip.setTelephoneReimburse(value);
			break;

		case PF:
			paySlip.setPf(value);
			break;

		case ESI:
			paySlip.setEsi(value);
			break;

		case DEDUCTION_TDS:
			paySlip.setDeductionTds(value);
			break;

		case ABSENT:
			paySlip.setAbsent(value);
			break;

		case DEDUCTION_LEAVE:
			paySlip.setDeductionLeave(value);
			break;

		case DEDUCTION_OTHERS:
			paySlip.setOtherDeductions(value);
			break;

		case TRANSPORT_ALLOWANCES:
			paySlip.setTransportAllowances(value);
			break;

		case LOAN:
			paySlip.setLoan(value);
			break;

		default:
			break;

		}
	}

	/**
	 * Get the pay slip data.
	 * 
	 * @param employeeId
	 *            If present then show pay data that employee
	 * @throws ServiceException
	 */
	public List<PaySlip> getPaySlipsList(Long employeeId) throws ServiceException {

		boolean specialRights = employeeId != null;

		Employee employee = !specialRights ? authenticationService.getAuthenticatedEmployee()
				: employeeService.findById(employeeId);

		if (employee == null)
			throw new ServiceException("Employee not found.");

		DateTime date = new DateTime().minusMonths(3);

		List<PaySlip> paySlips = new ArrayList<>();

		paySlips = !specialRights ? paySlipRepository.findByEmpIdAndYearAndMonthGreaterThanEqual(employee.getId(),
				date.getYear(), date.getMonthOfYear()) : paySlipRepository.findByEmpId(employee.getId());

		Collections.sort(paySlips, new PaySlipComparator());

		if (!specialRights) {
			if (paySlips.size() > 3)
				paySlips.remove(paySlips.size() - 1);
		}

		return paySlips;
	}

	/**
	 * Get the pay slip data.
	 * 
	 * @param paySlipId
	 * @throws ServiceException
	 */
	public PaySlip getPaySlip(Long paySlipId) throws ServiceException {

		PaySlip paySlip = paySlipRepository.findById(paySlipId);

		if (paySlip == null)
			throw new ServiceException("Pay slip Id not found.");

		Employee authEmployee = authenticationService.getAuthenticatedEmployee();

		Employee employee = employeeService.findByEmployeeId(paySlip.getEmpId());

		if (!(employee.getId() == authEmployee.getId() || authenticationService.isAdmin()
				|| authenticationService.isHR()))
			throw new ServiceException("This Pay slip not belong to you.");

		return paySlip;
	}

	/**
	 * Sets the data to pay slip.
	 * 
	 * @param month
	 * @param year
	 * 
	 * @throws ServiceException
	 */
	public void generatePaySlip(Integer month, Integer year) throws ServiceException {

		if (!authenticationService.isHrOrAdmin())
			throw new ServiceException("Sorry, Only Hr and Admin has rights to these.");

		if (month > 12 || month < 1)
			throw new ServiceException("Please enter correct month.");

		DateTime generationDate = DateUtil.resetTime(new DateTime().withMonthOfYear(month).withYear(year));

		List<Employee> employees = employeeService.findByActive();

		for (Employee employee : employees) {
			if (employee.getId() == 252) {
				System.out.println("PayrollService.generatePaySlip()");
			}
			PayrollFixed payrollFixed = null;

			List<PayrollFixed> payrolls = payrollFixedRepository.findByEmployeeAndValidFromAndDeleted(employee,
					generationDate, false);

			if (!payrolls.isEmpty())
				payrollFixed = payrolls.get(0);

			if (payrollFixed == null) {
				List<PayrollFixed> payrollFixeds = payrollFixedRepository.findByEmployeeAndActive(employee, true);

				if (!payrollFixeds.isEmpty())
					payrollFixed = payrollFixeds.get(0);
			}

			if (payrollFixed != null) {
				PayrollVariable payrollVariable = payrollVariableRepository.findByEmployeeAndMonthAndYear(employee,
						month, year);

				int absentDays = leaveService.getDeductionBetweenDates(employee, generationDate.toDate(),
						DateUtil.maxTime(generationDate.dayOfMonth().withMaximumValue().toDate()));

				PaySlip dbPaySlip = paySlipRepository.findByEmpIdAndMonthAndYear(employee.getId(), month, year);

				if (dbPaySlip != null)
					paySlipRepository.delete(dbPaySlip);

				PaySlip paySlip = new PaySlip(payrollFixed, payrollVariable);

				paySlip.setPf(getProRataPf(generationDate, absentDays, payrollFixed));
				paySlip.setEsi(getProRataEsi(generationDate, absentDays, payrollFixed));
				paySlip.setDeductionLeave(getLeaveDeductionValue(generationDate, absentDays, payrollFixed));
				paySlip.setMonth(month);
				paySlip.setYear(year);
				paySlip.setEmpId(employee.getId());
				paySlip.setWorkingDays((float) (generationDate.dayOfMonth().getMaximumValue() - absentDays));
				paySlipRepository.save(paySlip);
			}
		}
	}

	/**
	 * Get Pf based on pro rata i.e leave.
	 * 
	 * @param generationDate
	 * @param absentDays
	 * @param payrollFixed
	 * 
	 * @throws ServiceException
	 */
	private Float getProRataPf(DateTime generationDate, Integer absentDays, PayrollFixed payrollFixed)
			throws ServiceException {

		if (payrollFixed.getEmployeePf() == null || payrollFixed.getEmployeePf() < 0)
			return 0f;

		Float pfValue = payrollFixed.getEmployeePf();

		int presentDays = generationDate.dayOfMonth().getMaximumValue() - absentDays;

		if (payrollFixed.getIsPfInPercent())
			pfValue = (payrollFixed.getEmployeePf() * payrollFixed.getBasicPay()) / 100;

		Float oneDayPf = pfValue / generationDate.dayOfMonth().getMaximumValue();

		return oneDayPf * presentDays;
	}

	/**
	 * Get ESI based on pro rata i.e leave.
	 * 
	 * @param generationDate
	 * @param absentDays
	 * @param payrollFixed
	 * 
	 * @throws ServiceException
	 */
	private Float getProRataEsi(DateTime generationDate, Integer absentDays, PayrollFixed payrollFixed)
			throws ServiceException {

		if (payrollFixed.getEmployeeEsi() == null || payrollFixed.getEmployeeEsi() < 0)
			return 0f;

		Float esiValue = payrollFixed.getEmployeeEsi();

		int presentDays = generationDate.dayOfMonth().getMaximumValue() - absentDays;

		Float oneDayEsi = esiValue / generationDate.dayOfMonth().getMaximumValue();

		return oneDayEsi * presentDays;
	}

	/**
	 * Get ESI based on pro rata i.e leave.
	 * 
	 * @param generationDate
	 * @param absentDays
	 * @param payrollFixed
	 * 
	 * @throws ServiceException
	 */
	private Float getLeaveDeductionValue(DateTime generationDate, Integer absentDays, PayrollFixed payrollFixed)
			throws ServiceException {

		Float totalValue = 0f;

		totalValue += payrollFixed.getBasicPay() == null ? 0f : payrollFixed.getBasicPay();
		totalValue += payrollFixed.getHra() == null ? 0f : payrollFixed.getHra();
		totalValue += payrollFixed.getMedicalReimbursement() == null ? 0f : payrollFixed.getMedicalReimbursement();
		totalValue += payrollFixed.getSpecialAllowance() == null ? 0f : payrollFixed.getSpecialAllowance();
		totalValue += payrollFixed.getBusinessPromotion() == null ? 0f : payrollFixed.getBusinessPromotion();
		totalValue += payrollFixed.getLta() == null ? 0f : payrollFixed.getLta();
		totalValue += payrollFixed.getVehicleReimbursement() == null ? 0f : payrollFixed.getVehicleReimbursement();
		totalValue += payrollFixed.getAttireAllowance() == null ? 0f : payrollFixed.getAttireAllowance();
		totalValue += payrollFixed.getDriverAllowance() == null ? 0f : payrollFixed.getDriverAllowance();
		totalValue += payrollFixed.getFixedBonus() == null ? 0f : payrollFixed.getFixedBonus();
		totalValue += payrollFixed.getVehicleRunning() == null ? 0f : payrollFixed.getVehicleRunning();
		totalValue += payrollFixed.getTelReimbursement() == null ? 0f : payrollFixed.getTelReimbursement();

		if (totalValue < 0)
			return 0f;

		Float oneDayValue = totalValue / generationDate.dayOfMonth().getMaximumValue();

		return oneDayValue * absentDays;
	}

	/**
	 * Sets the fixed payroll of an employee.
	 * 
	 * @param PayrollFixed
	 * @return saved payroll UID
	 * @throws ServiceException
	 */
	@Transactional(rollbackFor = Exception.class)
	public long setFixedPayroll(PayrollFixed payrollFixed) throws ServiceException {

		if (!authenticationService.isHrOrAdmin())
			throw new ServiceException("Sorry, Only Hr and Admin has rights to these.");

		if (payrollFixed.getValidFrom() == null)
			throw new ServiceException("Please enter a valid date.");

		DateTime validDate = payrollFixed.getValidFrom().dayOfMonth().withMinimumValue();

		if (validDate.isAfter(new DateTime().dayOfMonth().withMinimumValue()))
			throw new ServiceException("Sorry, Future months are not allowed.");

		Employee employee = employeeService.findByEmployeeId(payrollFixed.getEmpId());

		List<PayrollFixed> payrollFixeds = payrollFixedRepository.findByEmployeeAndActive(employee, true);

		for (PayrollFixed payroll : payrollFixeds) {
			payroll.setActive(false);
			payroll.setExpiry(validDate);

			if (payroll.getValidFrom().isAfter(payroll.getExpiry()))
				throw new ServiceException("Sorry, Valid date not less than current current valid date.");

			if (payroll.getValidFrom().equals(payroll.getExpiry()))
				payroll.setDeleted(true);

			payrollFixedRepository.save(payroll);
		}

		payrollFixed.setEmployee(employee);
		payrollFixed.setActive(true);
		payrollFixed.setValidFrom(validDate);

		PayrollFixed savedFixedPayroll = payrollFixedRepository.save(payrollFixed);

		return savedFixedPayroll.getId();
	}

	/**
	 * Sets the variable payroll of an employee.
	 * 
	 * @param PayrollVariable
	 * 
	 * @return saved payroll UID
	 * @throws ServiceException
	 */
	@Transactional(rollbackFor = Exception.class)
	public long setVariablePayroll(PayrollVariable payrollVariable) throws ServiceException {

		if (!authenticationService.isHrOrAdmin())
			throw new ServiceException("Sorry, Only Hr and Admin has rights to these.");

		if (payrollVariable.getMonth() == null || payrollVariable.getYear() == null)
			throw new ServiceException("Sorry, Month or Year is missing.");

		if (payrollVariable.getMonth() > 12 || payrollVariable.getMonth() < 1)
			throw new ServiceException("Please enter correct month.");

		Employee employee = employeeService.findByEmployeeId(payrollVariable.getEmpId());

		PayrollVariable variable = payrollVariableRepository.findByEmployeeAndMonthAndYear(employee,
				payrollVariable.getMonth(), payrollVariable.getYear());

		if (variable != null)
			payrollVariable.setId(variable.getId());

		payrollVariable.setEmployee(employee);

		PayrollVariable savedVariablePayroll = payrollVariableRepository.save(payrollVariable);

		return savedVariablePayroll.getId();
	}

	/**
	 * Gets the fixed payroll of an employee.
	 * 
	 * @param Employee
	 *            id
	 * @param payroll
	 *            id
	 * 
	 * @return Fixed payroll or list of it accordingly
	 * @throws ServiceException
	 */
	public Object getFixedPayroll(Long empId, Long id) throws ServiceException {

		Employee authEmployee = authenticationService.getAuthenticatedEmployee();

		if (id != null) {
			PayrollFixed fixed = payrollFixedRepository.findById(id);

			if (fixed != null)
				return fixed;

			throw new ServiceException("Please enter a valid id.");
		}

		if (empId != null && authenticationService.isHrOrAdmin()) {
			Employee employee = employeeService.findByEmployeeId(empId);

			List<PayrollFixed> fixedPayrolls = payrollFixedRepository.findByEmployeeAndDeleted(employee, false);

			if (!fixedPayrolls.isEmpty())
				return fixedPayrolls;

			throw new ServiceException("Please enter a valid id.");
		}
		return payrollFixedRepository.findByEmployeeAndDeleted(authEmployee, false);
	}

	/**
	 * Gets the current fixed payroll of an employee.
	 * 
	 * @param Employee
	 *            id
	 * 
	 * @return Fixed payroll List
	 * @throws ServiceException
	 */
	public List<PayrollFixed> getCurrentFixedPayroll(Long empId) throws ServiceException {

		Employee authEmployee = authenticationService.getAuthenticatedEmployee();

		if (empId != null && authenticationService.isHrOrAdmin()) {
			Employee employee = employeeService.findByEmployeeId(empId);

			List<PayrollFixed> fixedPayrolls = payrollFixedRepository.findByEmployeeAndActive(employee, true);

			if (!fixedPayrolls.isEmpty())
				return fixedPayrolls;

			throw new ServiceException("Please enter a valid id.");
		}
		return payrollFixedRepository.findByEmployeeAndActive(authEmployee, true);
	}

	/**
	 * Gets the variable payroll of an employee.
	 * 
	 * @param Employee
	 *            id
	 * @param payroll
	 *            id
	 * 
	 * @return variable payroll or list of it accordingly
	 * @throws ServiceException
	 */
	public Object getVariablePayrollList(Long empId, Long id) throws ServiceException {

		Employee authEmployee = authenticationService.getAuthenticatedEmployee();

		if (id != null) {
			PayrollVariable variable = payrollVariableRepository.findById(id);

			if (variable != null)
				return variable;

			throw new ServiceException("Please enter a valid id.");
		}

		if (empId != null && authenticationService.isHrOrAdmin()) {
			Employee employee = employeeService.findByEmployeeId(empId);

			List<PayrollVariable> variablePayrolls = payrollVariableRepository.findByEmployee(employee);

			if (!variablePayrolls.isEmpty())
				return variablePayrolls;

			throw new ServiceException("Please enter a valid id.");
		}
		return payrollVariableRepository.findByEmployee(authEmployee);
	}

	/**
	 * Gets the variable payroll of an employee.
	 * 
	 * @param Employee
	 *            id
	 * 
	 * @return variable payroll
	 * @throws ServiceException
	 */
	public PayrollVariable getVariblePayroll(Long empId, Integer month, Integer year) throws ServiceException {

		if (month > 12 || month < 1)
			throw new ServiceException("Please enter correct month.");

		Employee authEmployee = authenticationService.getAuthenticatedEmployee();

		if (empId != null && authenticationService.isHrOrAdmin()) {
			Employee employee = employeeService.findByEmployeeId(empId);

			PayrollVariable payrollVariable = payrollVariableRepository.findByEmployeeAndMonthAndYear(employee, month,
					year);

			if (payrollVariable != null)
				return payrollVariable;

			throw new ServiceException("Please enter a valid id.");
		}
		return payrollVariableRepository.findByEmployeeAndMonthAndYear(authEmployee, month, year);
	}

	public PaySlipWrapper getLatestPaySlip() throws ServiceException {
		Employee employee = authenticationService.getAuthenticatedEmployee();
		if (employee == null)
			throw new ServiceException("Can't able to fetch PaySlip details.");

		PageRequest pageRequest = new PageRequest(0, 1);
		List<PaySlip> paySlips = paySlipRepository.findLatestPaySlip(employee.getId(), pageRequest);
		if (paySlips == null || paySlips.isEmpty()) {
			throw new ServiceException("No PaySlip available for an employee.");
		}
		return new PaySlipWrapper(employee, paySlips.get(0));
	}

	public PaySlipWrapper getEmployeePaySlip(Long paySlipId) throws ServiceException {
		authenticationService.isAdmin();
		PaySlip paySlip = paySlipRepository.findById(paySlipId);
		if (paySlip == null) {
			throw new ServiceException("No PaySlip available for an employee.");
		}
		Employee employee = employeeService.findById(paySlip.getEmpId());

		if (employee == null) {
			throw new ServiceException("The user is null");
		}
		if (employee.isActive() != null && !employee.isActive()) {
			throw new ServiceException("The user is not active");
		}
		return new PaySlipWrapper(employee, paySlip);
	}

}
