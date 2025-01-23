package com.ems.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javax.sql.rowset.serial.SerialException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.YearMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ems.configuration.PropConfig;
import com.ems.domain.CompanyCategory;
import com.ems.domain.Employee;
import com.ems.domain.Holiday;
import com.ems.domain.LeaveAccount;
import com.ems.domain.LeaveHistory;
import com.ems.domain.LeaveHistory.Status;
import com.ems.domain.LeaveHistoryDetails;
import com.ems.domain.LeaveType;
import com.ems.enums.LeaveName;
import com.ems.enums.MaritalStatus;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.HolidayRepository;
import com.ems.repository.LeaveHistoryRepository;
import com.ems.repository.LeaveTypesRepository;
import com.ems.servicefinder.utils.Constants;
import com.ems.servicefinder.utils.UserUtils;
import com.ems.wrappers.ApplyLeaveWrapper;
import com.ems.wrappers.ExcelReportWrapper;
import com.ems.wrappers.FileUploadWrapper;
import com.ems.wrappers.HolidayWrapper;
import com.ems.wrappers.LeaveAccountWrapper;
import com.ems.wrappers.LeaveApproveWrapper;
import com.ems.wrappers.LeaveAvailedWrapper;
import com.ems.wrappers.LeaveHistoryDetailsWrapper;
import com.ems.wrappers.LeaveHistoryWrapper;

@Service
@Transactional
public class LeaveService {

	@Autowired
	private LeaveTypesRepository leaveTypesRepository;

	@Autowired
	private LeaveHistoryRepository leaveHistoryRepository;

	@Autowired
	private PropConfig propConfig;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private LeaveAccountService leaveAccountService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private FileService fileService;

	@Autowired
	private LeaveValidatorService leaveValidatorService;
	
	
	//logger for printing logs on console
	public static final Logger logger = Logger.getLogger(LeaveService.class.getName());

	/**
	 * Find leave type by id.
	 *
	 * @param id
	 * 
	 * @return the LeaveType
	 */
	public LeaveType findTypeById(Integer id) {
		return leaveTypesRepository.findByTypeId(id);
	}

	/**
	 * Find leave history by employee id.
	 *
	 * @param employee
	 *            id
	 * 
	 * @return List of LeaveType
	 */
	public List<LeaveHistory> findHistoryByEmpId(Long id) {
		return leaveHistoryRepository.findByEmpId(id);
	}

	/**
	 * Delete leave History of an employee
	 * 
	 * @param employee
	 *            id
	 */
	public void deleteHistoryByEmpId(Long id) {
		leaveHistoryRepository.deleteByEmpId(id);
	}

	/**
	 * Fetch all LeaveType.
	 *
	 * 
	 * @return the LeaveType
	 * @throws ServiceException
	 */
	public List<LeaveType> getAllLeaveTypes() throws ServiceException {
		Employee employee = authenticationService.getAuthenticatedEmployee();
		ArrayList<String> leave = new ArrayList<>();
		leave.add(LeaveName.Casual_Leave.getLeaveName());
		leave.add(LeaveName.Sick_Leave.getLeaveName());
		leave.add(LeaveName.Civil_Duty_Leave.getLeaveName());
		int level = Integer.parseInt(employee.getDesignation().getLevel().split("_")[1]);
		if (level > 0
				&& UserUtils.isEmployeeJoiningGreaterThan(employee, new LocalDate(), Constants.CONFORMATION_MONTHS)) {
			leave.add(LeaveName.Privilege_Leave.getLeaveName());
			if (!employee.getMaritalStatus().equals(MaritalStatus.Married)) {
				leave.add(LeaveName.Marriage_Leave.getLeaveName());
			}
			leave.add(employee.getGender().equalsIgnoreCase("Male") ? LeaveName.Paternity_Leave.getLeaveName()
					: LeaveName.Maternity_Leave.getLeaveName());
		}

		return leaveTypesRepository.findAllLeave(leave);
	}

	// Return leave type
	public LeaveType getLeaveType(String leaveType) {
		List<LeaveType> leaveTypes = leaveTypesRepository.findAll();

		for (LeaveType leave : leaveTypes) {
			if (leave.getType().toLowerCase().contains(leaveType.toLowerCase())) {
				return leave;
			}
		}

		return null;
	}

	/**
	 * It is used in EMS for applying leaves. If validateApplication is true then it
	 * will validate the application.
	 * 
	 * @param leaveWrapper
	 * @param empId
	 * @param validateApplication
	 * @return
	 * @throws ServiceException
	 * @throws SerialException
	 */
	public List<LeaveHistoryDetailsWrapper> leaveApply(ApplyLeaveWrapper leaveWrapper, Long empId,
			boolean validateApplication) throws ServiceException, SerialException {
		logger.info("start applying leave of employee {}" + empId);

		Employee employee = empId != null ? employeeService.findByEmployeeId(empId)
				: authenticationService.getAuthenticatedEmployee();

		LeaveType leaveType = findTypeById(leaveWrapper.getTypeId());
		LocalDate startDate = new LocalDate(leaveWrapper.getFromDate());
		LocalDate endDate = new LocalDate(leaveWrapper.getToDate());
		float totalLeaveDays = leaveWrapper.getDayCount();

		// check employee leave applied from date or to date is not a holiday
		if (leaveWrapper.getTypeId() == 1 || leaveWrapper.getTypeId() == 2 || leaveWrapper.getTypeId() == 4) {
			logger.info("checking leave " + DateUtil.removeTime(leaveWrapper.getFromDate()) + " or "
					+ DateUtil.removeTime(leaveWrapper.getToDate()) + " is holiday or not");
			Boolean holiday = holidayService.checkIsHolidayOnFromDateOrToDate(leaveWrapper.getFromDate(),
					leaveWrapper.getToDate());
			logger.info("value of holiday " + holiday);
			if (holiday == true) {
				// check exact holiday date and show case message accordingly
				String holidayDate = holidayService.checkHolidayOnFromDateOrToDateForShowingMessage(
						leaveWrapper.getFromDate(), leaveWrapper.getToDate());
				throw new SerialException("You can't apply for leave on " + holidayDate
						+ ", as it is a holiday. Please choose another date.");
			}
		}


		if (validateApplication) {
			// It is not specific to leave types. But leave type specific
			// validation methods from leaveValidatorService should be called
			// after it.
			leaveValidatorService.validate(leaveWrapper, employee);
			
			// For Casual Leave Validation
			leaveValidatorService.validateCasualLeave(leaveWrapper, employee);
			
			// For Sick Leave Validation
			leaveValidatorService.validateSickLeave(leaveWrapper, employee);

			// For Privilege Leave validation
			leaveValidatorService.validatePrivilegeLeave(leaveWrapper, employee);

			// For Marriage Leave Validation
			leaveValidatorService.validateMarriageLeave(leaveWrapper, employee);

		}
		
//		if(!leaveWrapper.getDayCount().equals(totalLeaveDays)) {
//			logger.info("setting new total day count ");
////			endDate=new LocalDate(leaveWrapper.getToDate());
//			totalLeaveDays=leaveWrapper.getDayCount();
//		}

		// This List is used for cross year calcutaion. If it consist two entry
		// then it is two leaveHistoryDetailsWrapper for two years.
		List<LeaveHistoryDetailsWrapper> leaveHistoryDetailsWrapperList = new ArrayList<LeaveHistoryDetailsWrapper>();

		leaveWrapper.setDayCount(totalLeaveDays);
		logger.info("setting leaveWrapper.getDayCount() {} and totalLeaveDays {} "+leaveWrapper.getDayCount()+"  "+totalLeaveDays);
		logger.info("leaveHistoryDetailsWrapperList before switch "+leaveHistoryDetailsWrapperList);
		switch (LeaveName.getEnum(leaveType.getType())) {
		case Casual_Leave:
		case Sick_Leave:
		case Privilege_Leave:
			caluclatePrivilegeOrCasualOrSickLeave(startDate, endDate, leaveHistoryDetailsWrapperList, totalLeaveDays,
					leaveType.getType(), employee, validateApplication);
			break;

		case Marriage_Leave:
			caluculatePrivilegeLeave(employee.getCompanyCategory().getTotalMarriageLeave(), startDate, endDate,
					leaveHistoryDetailsWrapperList, employee, validateApplication, totalLeaveDays);
			break;

		case Maternity_Leave:
			caluculatePrivilegeLeave(employee.getCompanyCategory().getTotalMaternityLeave(), startDate, endDate,
					leaveHistoryDetailsWrapperList, employee, validateApplication, totalLeaveDays);
			break;

		case Paternity_Leave:
			caluculatePrivilegeLeave(employee.getCompanyCategory().getTotalPaternityLeave(), startDate, endDate,
					leaveHistoryDetailsWrapperList, employee, validateApplication, totalLeaveDays);
			break;

		case Civil_Duty_Leave:
			caluculatePrivilegeLeave(employee.getCompanyCategory().getTotalCivilDuityLeave(), startDate, endDate,
					leaveHistoryDetailsWrapperList, employee, validateApplication, totalLeaveDays);
			break;

		}
		logger.info("calling validateApplication if case "+leaveWrapper.getDayCount()+totalLeaveDays);
		logger.info("leaveHistoryDetailsWrapperList "+leaveHistoryDetailsWrapperList );
		if (validateApplication) {
			float dayCount = 0;
			if (leaveType.getType().equalsIgnoreCase(LeaveName.Sick_Leave.getLeaveName())
					|| leaveType.getType().equalsIgnoreCase(LeaveName.Casual_Leave.getLeaveName())) {
				for (LeaveHistoryDetailsWrapper leaveHistoryDetailsWrapper : leaveHistoryDetailsWrapperList) {
					dayCount += leaveHistoryDetailsWrapper.getDeductedCasualLeave()
							+ leaveHistoryDetailsWrapper.getDeductedSickLeave()
							+ leaveHistoryDetailsWrapper.getDeductedPrivilegeLeave()
							+ leaveHistoryDetailsWrapper.getDeductedLeaveWithOutPay();
				}
			}
			logger.info("inside validateApplication "+dayCount);
			LeaveHistory leave = new LeaveHistory(employee.getId(), leaveWrapper.getFromDate().toDate(),
					leaveWrapper.getToDate().toDate(), dayCount != 0 ? dayCount : leaveWrapper.getDayCount(),
					leaveWrapper.getEmployeeComments(), employee.getManagerId(), leaveType);
			if (leaveType.getType().equalsIgnoreCase("Sick Leave") && leaveWrapper.getFileContent() != null) {
				leave.setId(leaveHistoryRepository.getMaxLeaveHistoryId());
				uploadFile(leaveWrapper.getFileContent(), leave, employee);
			}
			logger.info("saving applied leave "+leave+" "+leave.getDayCount()+" "+leaveWrapper.getDayCount());
			leaveHistoryRepository.save(leave);

			if (propConfig.isIncludeLeaveMail())
				emailService.sendApplyLeaveMail(employee, leave);
		}

		return leaveHistoryDetailsWrapperList;
	}

	private void caluculatePrivilegeLeave(float totalLeave, LocalDate startDate, LocalDate endDate,
			List<LeaveHistoryDetailsWrapper> leaveHistoryDetailsWrapperList, Employee employee,
			boolean validateApplication, float totalLeaveDays) throws ServiceException {
		float remainDays = 0;

		// Divided because proper calculation incase less then 1 total leave
		// like in half day civil.
		if (totalLeave >= 1)
			remainDays = (Days.daysBetween(startDate, endDate).getDays() + 1) - totalLeave;
		else
			remainDays = totalLeaveDays - totalLeave;
		if (remainDays > 0) {
			startDate = startDate.plusDays((int) totalLeave);
			caluclatePrivilegeOrCasualOrSickLeave(startDate, endDate, leaveHistoryDetailsWrapperList, remainDays,
					LeaveName.Privilege_Leave.getLeaveName(), employee, validateApplication);
		}
	}

	private void calculateDeductedLeave(Employee employee, LocalDate startDate, LocalDate endDate, float totalLeaveDays,
			String leaveType, List<LeaveHistoryDetailsWrapper> leaveHistoryDetailsWrapperList) {
		logger.info("inside calculateDeductedLeave "+totalLeaveDays+" "+leaveHistoryDetailsWrapperList);
		if (leaveType.equalsIgnoreCase(LeaveName.Privilege_Leave.getLeaveName())) {
			calculatePrivelegeLeaveAndLWP(employee,
					leaveAccountService.findByEmpIdAndYear(employee.getId(), DateUtil.getFinancialYear(startDate)),
					leaveHistoryDetailsWrapperList, totalLeaveDays, startDate, endDate);
		} else {
			logger.info("calling calculateTotalCasualLeaveDeducted"+totalLeaveDays);
			calculateTotalCasualLeaveDeducted(employee,
					leaveAccountService.findByEmpIdAndYear(employee.getId(), DateUtil.getFinancialYear(startDate)),
					leaveHistoryDetailsWrapperList, startDate, endDate, totalLeaveDays,
					leaveType.equalsIgnoreCase(LeaveName.Sick_Leave.getLeaveName()));
		}
	}

	private void caluclatePrivilegeOrCasualOrSickLeave(LocalDate startDate, LocalDate endDate,
			List<LeaveHistoryDetailsWrapper> leaveHistoryDetailsWrapperList, float totalLeaveDays, String leaveTypes,
			Employee employee, boolean validateApplication) throws ServiceException {
		logger.info("inside caluclatePrivilegeOrCasualOrSickLeave");
		leaveAccountService.addLeaveAccount(employee, DateUtil.getFinancialYear(startDate), false);
		if (DateUtil.checkIfBothInSameFinancialYear(startDate, endDate)) {
			logger.info("inside if condition checkIfBothInSameFinancialYear");
			leaveHistoryDetailsWrapperList.add(new LeaveHistoryDetailsWrapper(DateUtil.getFinancialYear(startDate)));
			logger.info("calling calculateDeductedLeave method "+totalLeaveDays+" "+leaveHistoryDetailsWrapperList);
			calculateDeductedLeave(employee, startDate, endDate, totalLeaveDays, leaveTypes,
					leaveHistoryDetailsWrapperList);
		} else {
			LocalDate newDate = DateUtil.getFirstOrLastDateOfFinancialYear(startDate, false);
			float remainDays = Days.daysBetween(startDate, newDate).getDays() + 1;
			if (((totalLeaveDays * 10) % 10 == 5)) {
				remainDays -= .5f;
			}
			leaveHistoryDetailsWrapperList.add(new LeaveHistoryDetailsWrapper(DateUtil.getFinancialYear(startDate)));
			calculateDeductedLeave(employee, startDate, newDate, remainDays, leaveTypes,
					leaveHistoryDetailsWrapperList);

			leaveAccountService.addLeaveAccount(employee, DateUtil.getFinancialYear(endDate), false);

			totalLeaveDays -= remainDays;
			leaveHistoryDetailsWrapperList.add(new LeaveHistoryDetailsWrapper(DateUtil.getFinancialYear(endDate)));
			calculateDeductedLeave(employee, DateUtil.getFirstOrLastDateOfFinancialYear(endDate, true), endDate,
					totalLeaveDays, leaveTypes, leaveHistoryDetailsWrapperList);
		}
		if (leaveHistoryDetailsWrapperList.size() == 2
				&& leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 2)
						.isDeductedCasualOrSickLeave()
				&& leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 1)
						.isDeductedCasualOrSickLeave()) {
			logger.info("inside if case ");
			LeaveHistoryDetailsWrapper leaveHistoryDetailsWrapper = leaveHistoryDetailsWrapperList
					.get(leaveHistoryDetailsWrapperList.size() - 2);
			int backDays = (int) (leaveHistoryDetailsWrapper.getDeductedPrivilegeLeave()
					+ leaveHistoryDetailsWrapper.getDeductedLeaveWithOutPay());
			LocalDate endingDate = DateUtil.getFirstOrLastDateOfFinancialYear(leaveHistoryDetailsWrapper.getYear(),
					false);
			LocalDate startingDate = endingDate.minusDays(backDays - 1);

			Set<LocalDate> holiday = holidayService.getHolidayDatesBetween(employee, startingDate, endingDate);
			boolean isRemovedPL = true;
			for (LocalDate date = startingDate; date.isBefore(endingDate)
					|| date.isEqual(endingDate); date = date.plusDays(1)) {
				int day = date.getDayOfWeek();
				if (!holiday.contains(date) && day != DateTimeConstants.SATURDAY && day != DateTimeConstants.SUNDAY) {
					isRemovedPL = false;
					break;
				}
			}
			if (isRemovedPL) {
				leaveHistoryDetailsWrapper.setDeductedLeaveWithOutPay(0);
				leaveHistoryDetailsWrapper.setDeductedPrivilegeLeave(0);
			}
		}
		leaveHistoryDetailsWrapperList.removeIf(new Predicate<LeaveHistoryDetailsWrapper>() {
			@Override
			public boolean test(LeaveHistoryDetailsWrapper leaveHistoryDetailsWrapper) {
				return (leaveHistoryDetailsWrapper.getDeductedCasualLeave() == 0
						&& leaveHistoryDetailsWrapper.getDeductedSickLeave() == 0
						&& leaveHistoryDetailsWrapper.getDeductedPrivilegeLeave() == 0
						&& leaveHistoryDetailsWrapper.getDeductedLeaveWithOutPay() == 0);
			}
		});

		if (validateApplication && leaveHistoryDetailsWrapperList.isEmpty()) {
			throw new ServiceException("Already Holidays");
		}
	}

	/**
	 * Calculate deducted casual or sick leave and set into a
	 * LeaveHistoryDetailsWrapper.
	 * 
	 * @param employee
	 * @param leaveAccount
	 * @param leaveHistoryDetailsWrapper
	 * @param startDate
	 * @param endDate
	 * @param totalLeaveDays
	 * @param isSick
	 * @param isEmployeeValidForPrivilegeLeave
	 */
	private void calculateTotalCasualLeaveDeducted(Employee employee, LeaveAccount leaveAccount,
			List<LeaveHistoryDetailsWrapper> leaveHistoryDetailsWrapperList, LocalDate startDate, LocalDate endDate,
			float totalLeaveDays, boolean isSick) {
		logger.info("insdie calculateTotalCasualLeaveDeducted "+totalLeaveDays+" "+isSick+" "+leaveHistoryDetailsWrapperList.size());
		if (!isSick && leaveHistoryDetailsWrapperList.size() == 2
				&& leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 2)
						.getDeductedCasualLeave() == Constants.MAX_CASUAL_LEAVE_TAKEN_AT_A_TIME) {
			logger.info("calling calculatePrivelegeLeaveAndLWP ");
			calculatePrivelegeLeaveAndLWP(employee, leaveAccount, leaveHistoryDetailsWrapperList,
					(Days.daysBetween(startDate, endDate).getDays() + 1), startDate, endDate);
			return;
		}

		// int earnedLeave = Math.round(isSick ?
		// leaveAccount.getTotalSickLeave() :
		// leaveAccount.getTotalCasualLeave());
		float earnedLeave = isSick ? leaveAccount.getTotalSickLeave() : leaveAccount.getTotalCasualLeave();
		logger.info("assigning earned leave variable " + " " + earnedLeave + " " + isSick + " "
				+ leaveAccount.getTotalSickLeave() + " " + leaveAccount.getTotalCasualLeave()+" "+leaveHistoryDetailsWrapperList);
		// if (DateUtil.checkIfBothInSameFinancialYear(joiningDate, endDate)) {
		// LocalDate lastDateOfFinancialYear =
		// DateUtil.getFirstOrLastDateOfFinancialYear(joiningDate, false);
		// int months = Months.monthsBetween(joiningDate,
		// lastDateOfFinancialYear).getMonths() + 1;
		// earnedLeave *= (float) months / 12;
		// }

		float availedLeave = leaveAccount != null
				? (isSick ? leaveAccount.getAvailedSickLeave() : leaveAccount.getAvailedCasualLeave())
				: 0;
		float deductedLeave = 0;
		float previousYearDeductedLeave = 0;
		if (leaveHistoryDetailsWrapperList.size() == 2) {
			previousYearDeductedLeave = isSick
					? leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 2)
							.getDeductedSickLeave()
					: leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 2)
							.getDeductedCasualLeave();
		}
		boolean isCaluculatePrivilege = true;
		float fraction2 = 0;
		if (availedLeave < earnedLeave) {
			Set<LocalDate> holiday = holidayService.getHolidayDatesBetween(employee, startDate, endDate);
			isCaluculatePrivilege = false;
			float remains = 0;
			for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
				if ((!isSick && deductedLeave + previousYearDeductedLeave > Constants.MAX_CASUAL_LEAVE_TAKEN_AT_A_TIME)
						|| (availedLeave + deductedLeave) >= earnedLeave) {
					startDate = date;
					isCaluculatePrivilege = true;
					break;
				}
				int day = date.getDayOfWeek();
				if (!holiday.contains(date) && day != DateTimeConstants.SATURDAY && day != DateTimeConstants.SUNDAY) {
					if (totalLeaveDays == .5f) {
						remains = earnedLeave - availedLeave - deductedLeave;
						remains = new BigDecimal(remains).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
						if (remains >= 0.5f)
							deductedLeave = .5f;
						else {
							deductedLeave += remains;
							startDate = date;
							isCaluculatePrivilege = true;
							fraction2 = 0.5f - remains;
							break;
						}
					} else {
						// Calculating remaining leaves day by day.
						remains = earnedLeave - availedLeave - deductedLeave;
						remains = new BigDecimal(remains).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
						if (remains >= 1)
							deductedLeave++;
						else {
							deductedLeave += remains;
							startDate = date;
							isCaluculatePrivilege = true;
							fraction2 = 1 - remains;
							break;
						}
					}
				}
			}
		}
		if (isSick) {
			leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 1)
					.setDeductedSickLeave(deductedLeave);
		} else {
			leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 1)
					.setDeductedCasualLeave(deductedLeave);
		}
		if (isCaluculatePrivilege) {
			float fraction = totalLeaveDays - (int) totalLeaveDays;
			// float fraction2 = 1 - (deductedLeave - (int)deductedLeave);
			if (fraction2 == 0)
				fraction2 = 1;
			if (fraction == 0.0f) {
				calculatePrivelegeLeaveAndLWP(employee, leaveAccount, leaveHistoryDetailsWrapperList,
						(Days.daysBetween(startDate, endDate).getDays() + fraction2), startDate, endDate);
			} else {
				calculatePrivelegeLeaveAndLWP(employee, leaveAccount, leaveHistoryDetailsWrapperList,
						(Days.daysBetween(startDate, endDate).getDays() + fraction2), startDate, endDate);
			}
		}
	}

	/**
	 * Calculate Deducted privilege leave and LWP. set into a
	 * LeaveHistoryDetailsWrapper.
	 * 
	 * @param employee
	 * @param leaveAccount
	 * @param leaveHistoryDetailsWrapper
	 * @param totalLeaveDays
	 * @param isEmployeeValidForPrivilegeLeave
	 */
	private void calculatePrivelegeLeaveAndLWP(Employee employee, LeaveAccount leaveAccount,
			List<LeaveHistoryDetailsWrapper> leaveHistoryDetailsWrapperList, float totalLeaveDays,
			LocalDate startDate, LocalDate endDate) {
		logger.info("inside calculatePrivelegeLeaveAndLWP ");
		boolean isEmployeeValidForPrivilegeLeave = (Integer
				.parseInt(employee.getDesignation().getLevel().split("_")[1]) > 0
				&& (UserUtils.isEmployeeJoiningGreaterThan(employee, startDate, Constants.CONFORMATION_MONTHS)));

		LocalDate joiningDate = new LocalDate(employee.getJoiningDate());
//		LocalDate endDate = startDate.plusDays((int) totalLeaveDays - 1);
		Boolean crossMonth = startDate.getMonthOfYear() != endDate.getMonthOfYear() ? true : false;
		LocalDate joiningDateOrDesignationChangeDate = employee.getLevelChangedFromZero() == null ? joiningDate
				: employee.getLevelChangedFromZero().toLocalDate();
		int monthsTillNowFromJoiningOrYearStart = (Months
				.monthsBetween(DateUtil.checkIfBothInSameFinancialYear(joiningDateOrDesignationChangeDate, startDate)
						? joiningDateOrDesignationChangeDate.withDayOfMonth(1)
						: DateUtil.getFirstOrLastDateOfFinancialYear(startDate, true), endDate)
				.getMonths()) + 1;
		Integer totalPrivilegeLeavePerYear = employee.getCompanyCategory().getTotalPrivilegeLeave();
		float carryForward = 0f;
		if (leaveAccount.getTotalPrivilegeLeave() > totalPrivilegeLeavePerYear) {
			carryForward = leaveAccount.getTotalPrivilegeLeave() - totalPrivilegeLeavePerYear;
		}
		float totalPrivalegeLeave = monthsTillNowFromJoiningOrYearStart * ((float) totalPrivilegeLeavePerYear / 12)
				+ carryForward;
		float privelegeLeaveAvailed = leaveAccount.getAvailedPrivilegeLeave();
		float remainPrivilegeLeave = totalPrivalegeLeave - privelegeLeaveAvailed;

		// Incase applying for a less then 1.25 days for cross month 2nd month.
		if (crossMonth && (Days.daysBetween(endDate.dayOfMonth().withMinimumValue(), endDate).getDays()
				+ 1) < ((float) totalPrivilegeLeavePerYear / 12))
			remainPrivilegeLeave = remainPrivilegeLeave - (((float) totalPrivilegeLeavePerYear / 12)
					- (Days.daysBetween(endDate.dayOfMonth().withMinimumValue(), endDate).getDays() + 1));
		remainPrivilegeLeave = remainPrivilegeLeave < 0 ? 0 : remainPrivilegeLeave;

		float deductedPrivelegeLeave = 0;
		if (isEmployeeValidForPrivilegeLeave) {
			logger.info("ooooooooo "+isEmployeeValidForPrivilegeLeave+" "+deductedPrivelegeLeave+" "+remainPrivilegeLeave+" "+totalLeaveDays+" "+leaveHistoryDetailsWrapperList);
			deductedPrivelegeLeave = (remainPrivilegeLeave > totalLeaveDays) ? totalLeaveDays : remainPrivilegeLeave;
			leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 1)
					.setDeductedPrivilegeLeave(deductedPrivelegeLeave);
		}
		logger.info("ppppppppp "+leaveHistoryDetailsWrapperList);
		leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 1)
				.setDeductedLeaveWithOutPay((totalLeaveDays - deductedPrivelegeLeave));

		// This part is only used to store the cross month LWP distribution like
		// 3 LWP = 1LWP + 2LWP. Storing it to LeaveHistoryDetail.
		if (startDate.getMonthOfYear() != endDate.getMonthOfYear()) {
			logger.info("mmmmmmmm ");
			LocalDate firstMonthEndDate = startDate.dayOfMonth().withMaximumValue();
			// System.out.println(firstMonthEndDate);
			float totalThisMonth = Days.daysBetween(startDate, firstMonthEndDate).getDays() + 1;
			// System.out.println(totalThisMonth);

			int monthsTillNowFromJoiningOrYearStart2 = (Months
					.monthsBetween(
							DateUtil.checkIfBothInSameFinancialYear(joiningDateOrDesignationChangeDate, startDate)
									? joiningDateOrDesignationChangeDate.withDayOfMonth(1)
									: DateUtil.getFirstOrLastDateOfFinancialYear(startDate, true),
							firstMonthEndDate)
					.getMonths()) + 1;
			// Integer totalPrivilegeLeavePerYear2 =
			// employee.getCompanyCategory().getTotalPrivilegeLeave();
			// float carryForward2 = 0f;
			// if (leaveAccount.getTotalPrivilegeLeave() >
			// totalPrivilegeLeavePerYear) {
			// carryForward2 = leaveAccount.getTotalPrivilegeLeave() -
			// totalPrivilegeLeavePerYear;
			// }
			float totalPrivalegeLeave2 = monthsTillNowFromJoiningOrYearStart2
					* ((float) totalPrivilegeLeavePerYear / 12) + carryForward;
			float privelegeLeaveAvailed2 = leaveAccount.getAvailedPrivilegeLeave();
			float remainPrivilegeLeave2 = totalPrivalegeLeave2 - privelegeLeaveAvailed2;
			remainPrivilegeLeave2 = remainPrivilegeLeave2 < 0 ? 0 : remainPrivilegeLeave2;

			if (!isEmployeeValidForPrivilegeLeave)
				remainPrivilegeLeave2 = 0;
			// System.out.println(remainPrivilegeLeave2);
			float fraction = 1 - (totalLeaveDays - (int) totalLeaveDays);
			if (fraction == 1)
				fraction = 0;
			float firstMonthLeaveWithOutPay = totalThisMonth - remainPrivilegeLeave2 - fraction;
			firstMonthLeaveWithOutPay = new BigDecimal(firstMonthLeaveWithOutPay).setScale(2, BigDecimal.ROUND_HALF_UP)
					.floatValue();
			if (firstMonthLeaveWithOutPay < 0)
				firstMonthLeaveWithOutPay = 0;
			// System.out.println(firstMonthLeaveWithOutPay);
			// System.out.println(firstMonthLeaveWithOutPay -
			// deductedPrivelegeLeave);
			leaveHistoryDetailsWrapperList.get(leaveHistoryDetailsWrapperList.size() - 1)
					.setFirstMonthLeaveWithOutPay(firstMonthLeaveWithOutPay);
		}
	}

	/**
	 * Withdraw a leave.
	 * 
	 * @return List of LeaveHistoryWrapper
	 * @throws ServiceException
	 */
	public void withdrawLeave(Long id) throws ServiceException {

		Employee employee = authenticationService.getAuthenticatedEmployee();

		LeaveHistory history = leaveHistoryRepository.findById(id);

		if (history == null)
			throw new ServiceException("Leave not found.");

		if (!employee.getId().equals(history.getEmpId()))
			throw new ServiceException("This leave not belong to you.");

		history.setStatus(Status.WITHDRAWN);

		// Setting Withdrawn date
		history.setWithdrawnDate(DateTime.now());

		leaveHistoryRepository.save(history);

	}

	/**
	 * Approve Leave of user and withdraw leave of users after approval only by admin
	 * 
	 * @throws ServiceException
	 * @throws SerialException
	 *
	 * 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void approveLeave(LeaveApproveWrapper approveWrapper, boolean isAuttoApproved)
			throws ServiceException, SerialException {
		logger.info("inside approve leave leave id : {} " + approveWrapper.getLeaveId());
		LeaveHistory leave = leaveHistoryRepository.findById(approveWrapper.getLeaveId());

		if (leave == null)
			throw new ServiceException("Leave not found.");

//		if (!leave.getStatus().equals(Status.PENDING))
//			throw new ServiceException("Leave approved, rejected or widthdrawn");
		// // By Sarit Mukherjee
		// if
		// (leave.getLeaveType().getType().equalsIgnoreCase(LeaveName.Privilege_Leave.getLeaveName())
		// && Days.daysBetween(new LocalDate(), leave.fromDate().toLocalDate())
		// .getDays() < Constants.PRIVILEGE_LEAVE_DEADLINE_FOR_APPLY) {
		// leave.setStatus(Status.REJECTED);
		// throw new ServiceException("Privilege leave needs to be approved
		// before a minimum of "
		// + Constants.PRIVILEGE_LEAVE_DEADLINE_FOR_APPLY + " days.");
		// }

		if (!isAuttoApproved) {
			Employee approver = authenticationService.getAuthenticatedEmployee();

			if (!(authenticationService.isAdmin() || authenticationService.isHR()
					|| (leave.getManagerId().equals(approver.getId()))))
				throw new ServiceException("Only manager , HR and Admin have rights to approve leave");

			if (approveWrapper.isApproved()) {
				leave.setApprovedBy(approver.getId());
				leave.setApprovedDate(new Date());
			}
		} else {
			leave.setApprovedDate(new Date());
		}
		Employee approver = authenticationService.getAuthenticatedEmployee();
		// checking logged in user is admin or not so only they will revoke the leave
		// after approval
		if (leave.getStatus().equals(Status.APPROVED)) {
			if (!approver.getRole().getRoleName().equals("ADMIN")) {
				throw new ServiceException("Only Admin have rights to withdraw leave after approval");
			}
			leave.setStatus(Status.REVOKED);
			logger.info("set leave status revoked by admin {} " + leave.getStatus());
		} else {
			leave.setStatus(approveWrapper.isApproved() ? Status.APPROVED : Status.REJECTED);
		}

		// Setting rejected date if leave is rejected
		if (leave.getStatus().equals(Status.REJECTED))
			leave.setRejectedDate(DateTime.now());
		// setting withdrawn date if leave revoked after approval
		else if (leave.getStatus().equals(Status.REVOKED)) {
			leave.setWithdrawnDate(DateTime.now());
		}

		leave.setManagerComments(approveWrapper.getManagerComments());

		updateLeaveAccount(leave);

		leaveHistoryRepository.save(leave);
		
		if (propConfig.isIncludeLeaveMail()) {
			logger.info("inside propConfig.isIncludeLeaveMail()");
			emailService.sendApprovedLeaveMail(createHistoryWrapper(leave), approveWrapper.isApproved());
		}

	}

	/**
	 * Updates the leave account after leave approval and revoked the leave after approval
	 *
	 * @param leave
	 * 
	 * @throws ServiceException
	 * @throws SerialException
	 */
	public void updateLeaveAccount(LeaveHistory leave) throws ServiceException, SerialException {
		logger.info("inside updateLeaveAccount method");
		// if leave status is rejected it will return null
		if (leave.getStatus().equals(Status.REJECTED))
			return;

		ApplyLeaveWrapper applyLeaveWrapper = new ApplyLeaveWrapper(leave.fromDate(), leave.toDate(),
				leave.getDayCount(), leave.getLeaveType().getTypeId());
		List<LeaveHistoryDetails> leaveHistoryDetails = new ArrayList<LeaveHistoryDetails>();
		List<LeaveHistoryDetailsWrapper> leaveHistoryDetailsWrappersList = leaveApply(applyLeaveWrapper,
				leave.getEmpId(), false);

		for (LeaveHistoryDetailsWrapper leaveHistoryDetailsWrapper : leaveHistoryDetailsWrappersList) {
			LeaveAccount leaveAccount = leaveAccountService.findByEmpIdAndYear(leave.getEmpId(),
					leaveHistoryDetailsWrapper.getYear());

			if (leaveAccount == null)
				throw new ServiceException("Leave account not found.");
			
			//leave revoked case
			if (leave.getStatus().equals(Status.REVOKED)) {
				//get user leave data 
				logger.info("inside revoke case "+leave.getStatus());
				Object[] leaveData = leaveHistoryRepository.findLastLeaveOfEmployee(leave.getEmpId(),leave.getId());
				
				//calling setLeaveHistoryDetailsWrapperValueInRevokedCase method
				LeaveHistoryDetailsWrapper leaveHistoryDetailsWrapperValueInRevokedCase = setLeaveHistoryDetailsWrapperValueInRevokedCase(
						leaveHistoryDetailsWrapper, leaveData, applyLeaveWrapper.getTypeId());
				
				logger.info("leave Data "+leaveData+" "+leave.getLeaveType()+" "+leave.getId());
				
				leaveHistoryDetails.add(new LeaveHistoryDetails(leaveHistoryDetailsWrapperValueInRevokedCase));

				logger.info("inside revoked case");
				// swtich case for withdraw leave after approval
				switch (LeaveName.getEnum(leave.getLeaveType().getType())) {
				case Casual_Leave:
					leaveAccount.setAvailedCasualLeave(leaveAccount.getAvailedCasualLeave()
							- leaveHistoryDetailsWrapperValueInRevokedCase.getDeductedCasualLeave());
					break;
				case Sick_Leave:
					leaveAccount.setAvailedSickLeave(leaveAccount.getAvailedSickLeave()
							- leaveHistoryDetailsWrapperValueInRevokedCase.getDeductedSickLeave());
					break;
				case Privilege_Leave:
					leaveAccount.setAvailedPrivilegeLeave(leaveAccount.getAvailedPrivilegeLeave()
							- leaveHistoryDetailsWrapperValueInRevokedCase.getDeductedPrivilegeLeave());
					break;
				default:
				}

				leaveAccountService.saveAccount(leaveAccount);
				logger.info("leave successfully revoked by admin");
				
			} else {
				leaveHistoryDetails.add(new LeaveHistoryDetails(leaveHistoryDetailsWrapper));

				logger.info("approved case");
				switch (LeaveName.getEnum(leave.getLeaveType().getType())) {
				case Casual_Leave:
					leaveAccount.setAvailedCasualLeave(
							leaveAccount.getAvailedCasualLeave() + leaveHistoryDetailsWrapper.getDeductedCasualLeave());
					break;
				case Sick_Leave:
					leaveAccount.setAvailedSickLeave(
							leaveAccount.getAvailedSickLeave() + leaveHistoryDetailsWrapper.getDeductedSickLeave());
					break;
				default:
				}
				leaveAccount.setAvailedPrivilegeLeave(leaveAccount.getAvailedPrivilegeLeave()
						+ leaveHistoryDetailsWrapper.getDeductedPrivilegeLeave());
				leaveAccountService.saveAccount(leaveAccount);
				logger.info("leave successfully approved");
			}
		}
		logger.info("successfully updated leaveHistoryDetails");
		leave.setLeaveHistoryDetails(leaveHistoryDetails);
	}
	
	/**
	 * update LeaveHistoryDetailsWrapper in revoked case
	 * @param leaveHistoryDetailsWrapper
	 * @param leaveData
	 * @param leaveTypeId
	 * @return
	 */
	public LeaveHistoryDetailsWrapper setLeaveHistoryDetailsWrapperValueInRevokedCase(
	        LeaveHistoryDetailsWrapper leaveHistoryDetailsWrapper, Object[] leaveData, int leaveTypeId) {
		
		System.out.println("Object type not recognized: " + leaveData + " " + leaveTypeId);
		if (leaveData != null && leaveData.length > 0) {
			Object[] row = (Object[]) leaveData[0];
			if (leaveTypeId == 1) {
				leaveHistoryDetailsWrapper.setDeductedCasualLeave((Float) row[0]);
				leaveHistoryDetailsWrapper.setDeductedLeaveWithOutPay(0);
				leaveHistoryDetailsWrapper.setDeductedPrivilegeLeave(0);
			} else if (leaveTypeId == 2) {
				leaveHistoryDetailsWrapper.setDeductedSickLeave((Float) row[2]);
				leaveHistoryDetailsWrapper.setDeductedLeaveWithOutPay(0);
				leaveHistoryDetailsWrapper.setDeductedPrivilegeLeave(0);
			} else if (leaveTypeId == 4) {
				leaveHistoryDetailsWrapper.setDeductedLeaveWithOutPay(0);
				leaveHistoryDetailsWrapper.setDeductedPrivilegeLeave((Float) row[1]);
			}
		}
	    logger.info("setLeaveHistoryDetailsWrapperValueInRevokedCase successfully " + leaveTypeId + " " + leaveHistoryDetailsWrapper);

	    return leaveHistoryDetailsWrapper;
	}



	/**
	 * Get list of leave by your Team
	 *
	 * 
	 * @return List of LeaveHistoryWrapper
	 * @throws ServiceException
	 */
	public List<LeaveHistoryWrapper> getTeamAppliedLeaveList() throws ServiceException {

		List<LeaveHistory> leaveHistories = new ArrayList<>();
		Employee employee = authenticationService.getAuthenticatedEmployee();
		int level = Integer.parseInt(employee.getDesignation().getLevel().split("_")[1]);
		if (authenticationService.isAdmin() || authenticationService.isHR()) {
			if (authenticationService.isHR()) {
				// HR can approve leaves of 5 level high employees. 6 -> 10.
				// Also applies for admin human resource.
				leaveHistories = leaveHistoryRepository.findAllButHimselfWithLessLevel(
						level + Constants.MAXIMUM_LEVEL_UPTO_HR_CAN_APPROVE_LEAVE, employee.getId());
			} else {
				// Admin means management department(CEO,CTO) can approve leaves
				// of even their own level as they are of highest level 20.
				leaveHistories = leaveHistoryRepository.findAllButHimselfWithLessLevel(level + 1, employee.getId());
			}
		} else
			leaveHistories = leaveHistoryRepository
					.findByManagerId(authenticationService.getAuthenticatedEmployee().getId());

		List<LeaveHistoryWrapper> leaveHistoryWrappers = new ArrayList<LeaveHistoryWrapper>();

		for (LeaveHistory leaveHistory : leaveHistories) {
			LeaveHistoryWrapper leaveHistoryWrapper = createHistoryWrapper(leaveHistory);
			if (leaveHistoryWrapper != null)
				leaveHistoryWrappers.add(leaveHistoryWrapper);
		}

		// Collections.sort(leaveHistoryWrappers, new
		// Comparator<LeaveHistoryWrapper>() {
		// @Override
		// public int compare(LeaveHistoryWrapper e1, LeaveHistoryWrapper e2) {
		// if (e1.getStatus().equals(Status.PENDING) &&
		// e2.getStatus().equals(Status.PENDING)) {
		// return e1.getFromDate().compareTo(e2.getFromDate());
		// } else if
		// (e1.getStatus().equals(Status.PENDING)||e2.getStatus().equals(Status.PENDING))
		// {
		// return -1;
		// }
		// return e1.getFromDate().compareTo(e2.getFromDate());
		// }
		// });

		return leaveHistoryWrappers;

	}

	/**
	 * get list of leave of current logged in user
	 * 
	 * @return List of LeaveHistoryWrapper
	 * @throws ServiceException
	 */
	public Object getLeaveList(Long id) throws ServiceException {

		Employee employee = authenticationService.getAuthenticatedEmployee();

		if (id != null) {
			LeaveHistory history = leaveHistoryRepository.findById(id);

			if (history != null)
				return createHistoryWrapper(history);
			else
				throw new ServiceException("Leave history not found.");
		}

		List<LeaveHistory> leaveHistories = findHistoryByEmpId(employee.getId());

		List<LeaveHistoryWrapper> leaveHistoryWrappers = new ArrayList<LeaveHistoryWrapper>();

		for (LeaveHistory leaveHistory : leaveHistories) {
			LeaveHistoryWrapper leaveHistoryWrapper = createHistoryWrapper(leaveHistory);
			if (leaveHistoryWrapper != null)
				leaveHistoryWrappers.add(leaveHistoryWrapper);
		}

		return leaveHistoryWrappers;
	}

	/**
	 * Create leave history wrapper.
	 * 
	 * @param leaveHistory
	 * @param employee
	 * @return
	 * @throws ServiceException
	 */
	private LeaveHistoryWrapper createHistoryWrapper(LeaveHistory leaveHistory) throws ServiceException {
		Employee employee = null;
		try {
			employee = employeeService.findByEmployeeId(leaveHistory.getEmpId());
		} catch (Exception e) {
			return null;
		}

		LeaveHistoryWrapper historyWrapper = new LeaveHistoryWrapper(leaveHistory);

		historyWrapper.setEmployeeName(employee.getFullName());

		historyWrapper.setEmpCode(employee.getEmpCode());
		historyWrapper.setAttachmentUrl(fileService.getAssetCompleteUrl(
				leaveHistory.getAttachmentUrl() != null ? propConfig.getBaseContext() + leaveHistory.getAttachmentUrl()
						: null));

		// Setting Deduction Details.
		StringBuffer deductionDetails = new StringBuffer("");
		if (leaveHistory.getLeaveHistoryDetails() != null && !leaveHistory.getLeaveHistoryDetails().isEmpty()) {

			// For casual leave
			if (leaveHistory.getLeaveHistoryDetails().get(0).getDeductedCasualLeave() != 0.0f
					|| leaveHistory.getLeaveType().getTypeId().intValue() == 1) {
				deductionDetails
						.append(leaveHistory.getLeaveHistoryDetails().get(0).getDeductedCasualLeave() + "CL + ");
			}

			// For Sick leave
			if (leaveHistory.getLeaveHistoryDetails().get(0).getDeductedSickLeave() != 0.0f
					|| leaveHistory.getLeaveType().getTypeId().intValue() == 2) {
				deductionDetails.append(leaveHistory.getLeaveHistoryDetails().get(0).getDeductedSickLeave() + "SL + ");
			}

			// For Privilege leave
			if (leaveHistory.getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
					|| leaveHistory.getLeaveType().getTypeId().intValue() == 4) {
				deductionDetails
						.append(leaveHistory.getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() + "PL + ");
			}

			// // For Civil duty leave
			// if
			// ((leaveHistory.getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave()
			// != 0.0f
			// ||
			// leaveHistory.getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay()
			// != 0.0f)
			// && leaveHistory.getLeaveType().getTypeId().intValue() == 3) {
			// deductionDetails.append(employee.getCompanyCategory().getTotalCivilDuityLeave()
			// + "CDL + ");
			// }

			// For Civil duty leave
			if (leaveHistory.getLeaveType().getTypeId().intValue() == 3) {
				deductionDetails.append(employee.getCompanyCategory().getTotalCivilDuityLeave() + "CDL + ");
			}

			// For Marriage leave
			if (leaveHistory.getLeaveType().getTypeId().intValue() == 5) {

				if ((leaveHistory.getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
						|| leaveHistory.getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f)) {
					deductionDetails.append(employee.getCompanyCategory().getTotalMarriageLeave() + "ML + ");
				}
			}

			// For Maternity leave
			if (leaveHistory.getLeaveType().getTypeId().intValue() == 6) {

				if ((leaveHistory.getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
						|| leaveHistory.getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f)) {
					deductionDetails.append(employee.getCompanyCategory().getTotalMaternityLeave() + "MTL + ");
				}
			}

			// For Paternity leave
			if (leaveHistory.getLeaveType().getTypeId().intValue() == 7) {

				if ((leaveHistory.getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
						|| leaveHistory.getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f)) {
					deductionDetails.append(employee.getCompanyCategory().getTotalPaternityLeave() + "PTL + ");
				}
			}

			// For Leave without pay leave
			if (leaveHistory.getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f) {
				deductionDetails
						.append(leaveHistory.getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() + "LWP");
			} else {
				// Incase no LWP removing extra " + ".
				deductionDetails.setLength(deductionDetails.length() - 3);
			}
		}
		if (deductionDetails.length() == 0
				&& leaveHistory.getStatus().getStringValue().equalsIgnoreCase(Status.APPROVED.getStringValue())) {

			// For Civil duty leave
			if (leaveHistory.getLeaveType().getTypeId().intValue() == 3) {
				deductionDetails.append(employee.getCompanyCategory().getTotalCivilDuityLeave() + "CDL");
			}

			if (leaveHistory.getLeaveType().getTypeId().intValue() == 5) {
				// Incase less then total ML
				deductionDetails.append(
						(Days.daysBetween(leaveHistory.fromDate(), leaveHistory.toDate()).getDays() + 1) + "ML");
			}
			if (leaveHistory.getLeaveType().getTypeId().intValue() == 6) {
				// Incase less then total Maternity Leave
				deductionDetails.append(
						(Days.daysBetween(leaveHistory.fromDate(), leaveHistory.toDate()).getDays() + 1) + "MTL");
			}
			if (leaveHistory.getLeaveType().getTypeId().intValue() == 7) {
				// Incase less then total Paternity Leave
				deductionDetails.append(
						(Days.daysBetween(leaveHistory.fromDate(), leaveHistory.toDate()).getDays() + 1) + "PTL");
			}
		}
		if (leaveHistory.getStatus().getStringValue().equalsIgnoreCase(Status.REJECTED.getStringValue())
				&& leaveHistory.getRejectedDate() != null) {
			historyWrapper.setLeaveStatusDate(leaveHistory.getRejectedDate().toDate());
		}

		if (leaveHistory.getStatus().getStringValue().equalsIgnoreCase(Status.WITHDRAWN.getStringValue())
				&& leaveHistory.getWithdrawnDate() != null) {
			historyWrapper.setLeaveStatusDate(leaveHistory.getWithdrawnDate().toDate());
		}
		historyWrapper.setDeductionDetails(deductionDetails.toString());
		return historyWrapper;
	}

	/**
	 * Get Leave Account details by employee Id
	 * 
	 * @param id
	 *            the Employee Id
	 * @throws ServiceException
	 * @throws SerialException
	 */
	public LeaveAccountWrapper getLeaveAccountDetails(Long id) throws ServiceException, SerialException {
		Employee employee = null;

		// Using try catch as we also need to get leave account of resigned or
		// deleted = true employees.findById is used in many places so using try
		// catch.
		try {
			employee = employeeService.findById(id);
		} catch (ServiceException e) {
		}

		// This is used to get leave account of resigned employees with deleted
		// = true
		if (employee == null && (authenticationService.isHrOrAdmin())
				&& employeeService.findByEmployeeId(id).isDeleted())
			employee = employeeService.findByEmployeeId(id);

		if (employee == null)
			throw new ServiceException("Can't able to fetch leave account details");
		int level = Integer.parseInt(employee.getDesignation().getLevel().split("_")[1]);

		DateTime nowDate = new DateTime();
		boolean isValidForPrivelege = level > 0 && UserUtils.isEmployeeJoiningGreaterThan(employee,
				nowDate.toLocalDate(), Constants.CONFORMATION_MONTHS);

		LeaveAccount leaveAccount = leaveAccountService.findByEmpIdAndYear(employee.getId(),
				DateUtil.getFinancialYear(nowDate.toLocalDate()));
		// Start Counting leaves as per current date(Casual Leave and Sick
		// Leave)
		LocalDate joiningDate = new LocalDate(employee.getJoiningDate());
		// LocalDate endDateOfCurrentYear =
		// DateUtil.getFirstOrLastDateOfFinancialYear(new LocalDate(), false);
		// float totalSickLeaves = leaveAccount.getTotalSickLeave();
		// totalSickLeaves = calculateLeaveConsideringJoiningDate(joiningDate,
		// endDateOfCurrentYear, totalSickLeaves);
		// float totalCasualLeaves = leaveAccount.getTotalCasualLeave();
		// totalCasualLeaves = calculateLeaveConsideringJoiningDate(joiningDate,
		// endDateOfCurrentYear, totalCasualLeaves);

		if (leaveAccount == null)
			throw new ServiceException("No leave account found for the current year. Contact administrator.");

		LeaveAccountWrapper leaveAccountWrapper = new LeaveAccountWrapper(leaveAccount.getTotalSickLeave(),
				leaveAccount.getTotalCasualLeave(), leaveAccount.getTotalPrivilegeLeave(),
				leaveAccount.getAvailedSickLeave(), leaveAccount.getAvailedCasualLeave(),
				leaveAccount.getAvailedPrivilegeLeave(), employee.getCompanyCategory(), isValidForPrivelege);
		nowDate = nowDate.minusMonths(1);
		for (int i = 0; i < 3; i++) {
			if (nowDate
					.isBefore(new DateTime(joiningDate.getYear(), joiningDate.getMonthOfYear(),
							joiningDate.getDayOfMonth(), 0, 0))
					&& nowDate.getMonthOfYear() != joiningDate.getMonthOfYear()) {
				nowDate = nowDate.plusMonths(1);
				continue;
			}
			DateTime monthStartDate = DateUtil.resetTime(nowDate.withDayOfMonth(1));
			DateTime monthEndDate = DateUtil.resetTime(nowDate.dayOfMonth().withMaximumValue());

			leaveAccount = leaveAccountService.findByEmpIdAndYear(employee.getId(),
					DateUtil.getFinancialYear(nowDate.toLocalDate()));
			if (leaveAccount == null) {
				leaveAccountService.addLeaveAccount(employee, DateUtil.getFinancialYear(nowDate.toLocalDate()), false);
				leaveAccount = leaveAccountService.findByEmpIdAndYear(employee.getId(),
						DateUtil.getFinancialYear(nowDate.toLocalDate()));
			}

			boolean afterLevelChangedFromZero = false;
			if (employee.getLevelChangedFromZero() != null)
				afterLevelChangedFromZero = nowDate.isAfter(employee.getLevelChangedFromZero());

			isValidForPrivelege = afterLevelChangedFromZero && UserUtils.isEmployeeJoiningGreaterThan(employee,
					nowDate.toLocalDate(), Constants.CONFORMATION_MONTHS);
			// Start Counting leaves as per current date(Privilege Leave)
			LocalDate joiningDateOrDesignationChangeDate = employee.getLevelChangedFromZero() == null ? joiningDate
					: employee.getLevelChangedFromZero().toLocalDate();
			float monthsTillNowFromJoiningOrYearStart = (Months.monthsBetween(
					DateUtil.checkIfBothInSameFinancialYear(joiningDateOrDesignationChangeDate, nowDate.toLocalDate())
							? joiningDateOrDesignationChangeDate.withDayOfMonth(1)
							: DateUtil.getFirstOrLastDateOfFinancialYear(nowDate.toLocalDate(), true),
					nowDate.toLocalDate()).getMonths()) + 1;
			Integer totalPrivilegeLeavePerYear = employee.getCompanyCategory().getTotalPrivilegeLeave();
			float carryForward = 0f;
			if (leaveAccount.getTotalPrivilegeLeave() > totalPrivilegeLeavePerYear) {
				carryForward = leaveAccount.getTotalPrivilegeLeave() - totalPrivilegeLeavePerYear;
			}
			float totalPrivalegeLeave = monthsTillNowFromJoiningOrYearStart * ((float) totalPrivilegeLeavePerYear / 12)
					+ carryForward;

			float privelegeLeaveAvailed = leaveAccount.getAvailedPrivilegeLeave();
			float remainPrivilegeLeave = totalPrivalegeLeave - privelegeLeaveAvailed;
			remainPrivilegeLeave = remainPrivilegeLeave < 0 ? 0 : remainPrivilegeLeave;

			float remainingCasualLeave = leaveAccount.getTotalCasualLeave() - leaveAccount.getAvailedCasualLeave();
			remainingCasualLeave = remainingCasualLeave < 0 ? 0 : remainingCasualLeave;

			leaveAccountWrapper.getLeaveAvailedWrappers().add(new LeaveAvailedWrapper(
					new YearMonth(nowDate.getYear(), nowDate.getMonthOfYear()).monthOfYear().getAsText(new Locale("en"))
							+ " " + nowDate.getYear(),
					remainingCasualLeave, isValidForPrivelege ? remainPrivilegeLeave : 0));
			LeaveAvailedWrapper leaveAvailedWrapper = leaveAccountWrapper.getLeaveAvailedWrappers()
					.get(leaveAccountWrapper.getLeaveAvailedWrappers().size() - 1);

			List<LeaveHistory> leaveHistoryList = leaveHistoryRepository.findAllDataByMonthWithToDate(employee.getId(),
					Status.APPROVED, monthStartDate.toDate(), monthEndDate.toDate());

			for (LeaveHistory leaveHistory : leaveHistoryList) {
				for (LeaveHistoryDetails leaveHistoryDetails : leaveHistory.getLeaveHistoryDetails()) {
					if (leaveHistoryDetails.getYear() == DateUtil.getFinancialYear(nowDate.toLocalDate())) {
						boolean isFromDateMonthEqualtoDateMonth = leaveHistory.fromDate()
								.getMonthOfYear() == leaveHistory.toDate().getMonthOfYear();
						if (isFromDateMonthEqualtoDateMonth) {
							leaveAvailedWrapper.setLeaveAvailed(leaveAvailedWrapper.getLeaveAvailed()
									+ (leaveHistoryDetails.getDeductedCasualLeave()
											+ leaveHistoryDetails.getDeductedPrivilegeLeave()));
							leaveAvailedWrapper.setLeaveWithOutPay(leaveAvailedWrapper.getLeaveWithOutPay()
									+ leaveHistoryDetails.getDeductedLeaveWithOutPay());
						} else {
							boolean isFromDateMonthEqualtonowDate = leaveHistory.fromDate().getMonthOfYear() == nowDate
									.getMonthOfYear();
							if (leaveHistory.getLeaveType().getType().equals(LeaveName.Casual_Leave.getLeaveName())
									|| leaveHistory.getLeaveType().getType()
											.equals(LeaveName.Sick_Leave.getLeaveName())) {

								Set<LocalDate> holiday = holidayService.getHolidayDatesBetween(employee,
										monthStartDate.toLocalDate(), monthEndDate.toLocalDate());
								int totalDays = Days.daysBetween(leaveHistory.fromDate(), leaveHistory.toDate())
										.getDays() + 1;
								int holidaysCount = getHolidaysCountBetweenTwoDate(
										isFromDateMonthEqualtonowDate ? leaveHistory.fromDate().toLocalDate()
												: monthStartDate.toLocalDate(),
										isFromDateMonthEqualtonowDate ? monthEndDate.toLocalDate()
												: leaveHistory.toDate().toLocalDate(),
										(totalDays - leaveHistory.getDayCount().intValue()), holiday);

								float remainDays = (isFromDateMonthEqualtonowDate
										? Days.daysBetween(leaveHistory.fromDate(), monthEndDate).getDays()
										: Days.daysBetween(monthStartDate, leaveHistory.toDate()).getDays()) + 1;
								float deducetdeDays = leaveHistory.getLeaveType().getType()
										.equals(LeaveName.Sick_Leave.getLeaveName())
												? leaveHistoryDetails.getDeductedSickLeave()
												: leaveHistoryDetails.getDeductedCasualLeave();
								if (isFromDateMonthEqualtonowDate) {

									float deductedCasualDays = remainDays - (deducetdeDays + holidaysCount) > 0
											? (deducetdeDays + holidaysCount)
											: (remainDays);

									float deductedPrivilegeDays = (remainDays > deductedCasualDays)
											? ((remainDays - deductedCasualDays)
													- leaveHistoryDetails.getDeductedPrivilegeLeave() > 0
															? leaveHistoryDetails.getDeductedPrivilegeLeave()
															: (remainDays - deductedCasualDays))
											: (0);
									float deductedLwpDays = (remainDays > (deductedCasualDays + deductedPrivilegeDays))
											? ((remainDays - deductedCasualDays - deductedPrivilegeDays)
													- leaveHistoryDetails.getDeductedLeaveWithOutPay()) > 0
															? leaveHistoryDetails.getDeductedLeaveWithOutPay()
															: (remainDays - deductedCasualDays - deductedPrivilegeDays)
											: 0;

									leaveAvailedWrapper.setLeaveWithOutPay(leaveAvailedWrapper.getLeaveWithOutPay()
											+ (leaveHistoryDetails.getFirstMonthLeaveWithOutPay()));
									leaveAvailedWrapper
											.setLeaveAvailed(
													leaveAvailedWrapper.getLeaveAvailed()
															+ (leaveHistory.getLeaveType().getType()
																	.equals(LeaveName.Sick_Leave.getLeaveName()) ? 0
																			: (deductedCasualDays - holidaysCount))
															+ remainDays
															- (leaveHistoryDetails.getFirstMonthLeaveWithOutPay()
																	+ holidaysCount)
															- (deductedCasualDays - holidaysCount));
								} else {

									// float deductedLwpDays = (remainDays >
									// leaveHistoryDetails
									// .getDeductedLeaveWithOutPay())
									// ?
									// leaveHistoryDetails.getDeductedLeaveWithOutPay()
									// : remainDays;

									float deductedLwpDays = leaveHistoryDetails.getDeductedLeaveWithOutPay()
											- leaveHistoryDetails.getFirstMonthLeaveWithOutPay();

									float deductedPrivilegeDays = (remainDays > deductedLwpDays)
											? ((remainDays - deductedLwpDays)
													- leaveHistoryDetails.getDeductedPrivilegeLeave() > 0
															? leaveHistoryDetails.getDeductedPrivilegeLeave()
															: (remainDays - deductedLwpDays))
											: (0);
									float deductedCasualDays = (remainDays > (deductedLwpDays + deductedPrivilegeDays))
											? ((remainDays - (deductedLwpDays + deductedPrivilegeDays))
													- (deducetdeDays + holidaysCount)) > 0
															? (deducetdeDays + holidaysCount)
															: (remainDays - (deductedLwpDays + deductedPrivilegeDays))
											: 0;
									leaveAvailedWrapper.setLeaveWithOutPay(
											leaveAvailedWrapper.getLeaveWithOutPay() + deductedLwpDays);

									// Holiday deduction holidaysCount is
									// optional as CL may already be consumed in
									// the last month.
									Set<LocalDate> holiday2 = holidayService.getHolidayDatesBetween(employee,
											monthStartDate.toLocalDate().minusMonths(1).dayOfMonth().withMinimumValue(),
											monthEndDate.toLocalDate().minusMonths(1).dayOfMonth().withMaximumValue());

									int holidaysCount2 = getHolidaysCountBetweenTwoDate(
											leaveHistory.fromDate().toLocalDate(),
											monthEndDate.toLocalDate().minusMonths(1).dayOfMonth().withMaximumValue(),
											(totalDays - leaveHistory.getDayCount().intValue()), holiday2);
									if ((totalDays - remainDays - holidaysCount2) >= leaveHistoryDetails
											.getDeductedCasualLeave())
										holidaysCount = 0;
									// System.out.println(totalDays - remainDays
									// - holidaysCount2);

									leaveAvailedWrapper.setLeaveAvailed(leaveAvailedWrapper.getLeaveAvailed()
											+ (leaveHistory.getLeaveType().getType()
													.equals(LeaveName.Sick_Leave.getLeaveName()) ? 0
															: (deductedCasualDays - holidaysCount))
											+ deductedPrivilegeDays);
								}
							} else {
								if (leaveHistoryDetails.getDeductedPrivilegeLeave() != 0
										|| leaveHistoryDetails.getDeductedLeaveWithOutPay() != 0) {
									float plPrevMonth = 0.0f;
									float remainDays = (isFromDateMonthEqualtonowDate
											? Days.daysBetween(leaveHistory.fromDate(), monthEndDate).getDays()
											: Days.daysBetween(monthStartDate, leaveHistory.toDate()).getDays()) + 1;
									if (isFromDateMonthEqualtonowDate) {
										remainDays -= getLeaveDescription(leaveHistory.getLeaveType(),
												employee.getCompanyCategory());
									} else {
										// remainDays = (remainDays >
										// (leaveHistoryDetails.getDeductedPrivilegeLeave()
										// +
										// leaveHistoryDetails.getDeductedLeaveWithOutPay()))
										// ?
										// (leaveHistoryDetails.getDeductedPrivilegeLeave()
										// +
										// leaveHistoryDetails.getDeductedLeaveWithOutPay())
										// : remainDays;
										float remainDaysPrev = Days
												.daysBetween(leaveHistory.fromDate(),
														monthEndDate.minusMonths(1).dayOfMonth().withMaximumValue())
												.getDays() + 1;
										plPrevMonth = remainDaysPrev
												- getLeaveDescription(leaveHistory.getLeaveType(),
														employee.getCompanyCategory())
												- leaveHistoryDetails.getFirstMonthLeaveWithOutPay();
										if (plPrevMonth <= 0)
											plPrevMonth = 0;

									}
									if (remainDays > 0) {
										// remainDays -=
										// ((!isFromDateMonthEqualtonowDate
										// ||
										// (((leaveHistoryDetails.getDeductedPrivilegeLeave()
										// +
										// leaveHistoryDetails.getDeductedLeaveWithOutPay())
										// * 10)
										// % 10 != 5)) ? 0 : .5);

										// float deductedLeave = (remainDays -
										// (isFromDateMonthEqualtonowDate
										// ?
										// leaveHistoryDetails.getDeductedPrivilegeLeave()
										// :
										// leaveHistoryDetails.getDeductedLeaveWithOutPay()))
										// > 0
										// ? (isFromDateMonthEqualtonowDate
										// ?
										// leaveHistoryDetails.getDeductedPrivilegeLeave()
										// :
										// leaveHistoryDetails.getDeductedLeaveWithOutPay())
										// : remainDays;
										float deductedLeave = remainDays
												- leaveHistoryDetails.getFirstMonthLeaveWithOutPay();
										// leaveAvailedWrapper.setLeaveAvailed(leaveAvailedWrapper.getLeaveAvailed()
										// + (isFromDateMonthEqualtonowDate ?
										// deductedLeave
										// : (remainDays - deductedLeave)));

										leaveAvailedWrapper.setLeaveAvailed(leaveAvailedWrapper.getLeaveAvailed()
												+ (isFromDateMonthEqualtonowDate ? deductedLeave
														: leaveHistoryDetails.getDeductedPrivilegeLeave()
																- plPrevMonth));

										leaveAvailedWrapper.setLeaveWithOutPay(leaveAvailedWrapper.getLeaveWithOutPay()
												+ (isFromDateMonthEqualtonowDate
														? leaveHistoryDetails.getFirstMonthLeaveWithOutPay()
														: leaveHistoryDetails.getDeductedLeaveWithOutPay()
																- leaveHistoryDetails.getFirstMonthLeaveWithOutPay()));
									}
								}
							}
						}
					}
				}
			}
			nowDate = nowDate.plusMonths(1);
		}
		return leaveAccountWrapper;
	}

	private float getLeaveDescription(LeaveType leaveType, CompanyCategory companyCategory) throws SerialException {
		float days = 0;
		switch (LeaveName.getEnum(leaveType.getType())) {
		case Marriage_Leave:
			days = companyCategory.getTotalMarriageLeave().floatValue();
			break;
		case Maternity_Leave:
			days = companyCategory.getTotalMaternityLeave().floatValue();
			break;
		case Paternity_Leave:
			days = companyCategory.getTotalPaternityLeave().floatValue();
			break;
		case Civil_Duty_Leave:
			days = companyCategory.getTotalCivilDuityLeave().floatValue();
			break;
		default:
			break;
		}
		return days;
	}

	private int getHolidaysCountBetweenTwoDate(LocalDate fromDate, LocalDate toDate, int totalHolidays,
			Set<LocalDate> holiday) {
		int days = 0;
		// System.out.println("fromDate " + fromDate + " toDate " + toDate + "
		// totalHolidays : " + totalHolidays);
		for (LocalDate date = fromDate; date.isBefore(toDate) || date.isEqual(toDate); date = date.plusDays(1)) {
			if (totalHolidays <= days)
				return days;
			int day = date.getDayOfWeek();
			if ((holiday.contains(date) || day == DateTimeConstants.SATURDAY || day == DateTimeConstants.SUNDAY)) {
				days++;
			}
		}
		return days;
	}

	/**
	 * Fetches the number of leaves taken by an employee
	 *
	 * @param Employee
	 *            id
	 * @param Start
	 *            date
	 * @param End
	 *            date
	 */
	public Float getLeaveTaken(long id, DateTime startDate, DateTime endDate, LeaveType leaveType) {
		Float leavesTaken = 0f;

		List<LeaveHistory> takenLeaves = leaveHistoryRepository.findByEmpIdAndLeaveTypeAndStatusAndToDateBetween(id,
				leaveType, Status.APPROVED, startDate.toDate(), endDate.toDate());

		for (LeaveHistory leaveHistory : takenLeaves)
			leavesTaken = leavesTaken + leaveHistory.getDayCount();

		return leavesTaken;
	}

	/**
	 * Fetches the number of leaves taken by an employee
	 *
	 * @param Start
	 *            date
	 * @param End
	 *            date
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public List<ExcelReportWrapper> getLeaveBetween(String startDateString, String endDateString)
			throws ServiceException, ParseException {
		Date startDate = new SimpleDateFormat("MM-dd-yyyy").parse(startDateString);
		Date endDate = new SimpleDateFormat("MM-dd-yyyy").parse(endDateString);

		if (startDate == null || endDate == null)
			throw new ServiceException("Cannot parse date. Please send in MM-dd-yyyy format.");

		List<ExcelReportWrapper> reportWrappers = new ArrayList<>();

		List<LeaveHistory> histories = leaveHistoryRepository.findByFromDateBetweenOrToDateBetween(startDate, endDate,
				startDate, endDate);

		for (LeaveHistory leaveHistory : histories) {
			ExcelReportWrapper wrapper = new ExcelReportWrapper();
			wrapper.setEmployee(employeeService.findByEmployeeId(leaveHistory.getEmpId()));
			wrapper.setLeave(leaveHistory);

			reportWrappers.add(wrapper);
		}

		return reportWrappers;
	}

	/**
	 * Fetches the leaves taken by an employee b/w dates
	 *
	 * @param Start
	 *            date
	 * @param End
	 *            date
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public List<LeaveHistory> getLeaveBetweenDates(Employee employee, Date startDate, Date endDate)
			throws ServiceException {
		return leaveHistoryRepository.findByEmpIdAndToDateBetween(employee.getId(), startDate, endDate);
	}

	/**
	 * Fetches the deductions b/w dates
	 *
	 * @param Start
	 *            date
	 * @param End
	 *            date
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public int getDeductionBetweenDates(Employee employee, Date startDate, Date endDate) throws ServiceException {
		int deductions = 0;

		for (LeaveHistory leaveHistory : getLeaveBetweenDates(employee, startDate, endDate))
			deductions = deductions + leaveHistory.getDeductableDayCount();

		return deductions;
	}

	private void uploadFile(MultipartFile fileContent, LeaveHistory leave, Employee employee) throws ServiceException {
		UserUtils.validateFileSize(1024, fileContent, "file", true, false, false);
		try {
			String fileName = employee.getFirstName() + "(" + employee.getEmpCode() + ")_Medical";

			String standardFileName = UserUtils.getAssetStandardName(fileContent.getOriginalFilename(), fileName);

			String imageUrl = fileService.storeAsset(leave.getId().longValue(), fileContent.getBytes(),
					standardFileName, "leave");

			leave.setAttachmentUrl(imageUrl);

		} catch (Exception e) {
			throw new ServiceException("Error while uploading image.");
		}
	}

	/**
	 * Upload leave Attachment Url associated: /leave/upload
	 * 
	 * @param Leave
	 *            Id
	 * @param FileUploadWrapper
	 * @throws ServiceException
	 */
	public String uploadAttachment(FileUploadWrapper uploadWrapper, Long id) throws ServiceException {

		UserUtils.validateFileSize(1024, uploadWrapper.getFileContent(), "file", true, false, false);

		try {

			LeaveHistory leave = leaveHistoryRepository.findById(id);

			if (leave == null)
				throw new ServiceException("Leave not found.");

			Employee employee = employeeService.findById(leave.getEmpId());

			String fileName = employee.getFirstName() + "(" + employee.getEmpCode() + ")_Medical";

			String standardFileName = UserUtils
					.getAssetStandardName(uploadWrapper.getFileContent().getOriginalFilename(), fileName);

			String imageUrl = fileService.storeAsset(leave.getId().longValue(),
					uploadWrapper.getFileContent().getBytes(), standardFileName, "leave");
			leave.setAttachmentUrl(imageUrl);

			leaveHistoryRepository.save(leave);

			return fileService.getAssetCompleteUrl(leave.getAttachmentUrl());
		} catch (Exception e) {
			throw new ServiceException("Error while uploading image.");
		}
	}

	/**
	 * Gets pending leave count.
	 * 
	 * 
	 * @return count
	 * @throws ServiceException
	 */
	public int getTeamAppliedLeaveCount() throws ServiceException {

		List<LeaveHistory> leaveHistories = leaveHistoryRepository
				.findByManagerIdAndStatus(authenticationService.getAuthenticatedEmployee().getId(), Status.PENDING);

		if (authenticationService.isAdmin() || authenticationService.isHR()) {
			int level = Integer.parseInt(
					authenticationService.getAuthenticatedEmployee().getDesignation().getLevel().split("_")[1]);

			if (authenticationService.isHR()) {
				// HR can approve leaves of 5 level high employees. 6 -> 10.
				// Also applies for admin HR.
				leaveHistories = leaveHistoryRepository.findAllButHimselfWithLessLevelAndStatus(
						level + Constants.MAXIMUM_LEVEL_UPTO_HR_CAN_APPROVE_LEAVE,
						authenticationService.getAuthenticatedEmployee().getId(), "PENDING");
			} else {
				// Admin means management department(CEO,CTO) can approve leaves
				// of even their own level as they are of highest level 20.
				leaveHistories = leaveHistoryRepository.findAllButHimselfWithLessLevelAndStatus(level + 1,
						authenticationService.getAuthenticatedEmployee().getId(), "PENDING");
			}
		}

		return leaveHistories.size();
	}

	/**
	 * Checks if its a casual leave
	 *
	 * @param leaveType
	 */
	public Boolean isCasualLeave(String leaveType) {
		if (leaveType.toLowerCase().contains("casual"))
			return true;

		return false;
	}

	/**
	 * Checks if its a Sick Leave
	 *
	 * @param leaveType
	 */
	public Boolean isSickLeave(String leaveType) {
		if (leaveType.toLowerCase().contains("sick"))
			return true;

		return false;
	}

	public List<LeaveHistory> getExpireLeaveIfNotRejected() {
		return leaveHistoryRepository.findAllAutoApprovedLeaveList(Status.PENDING, new Date());
	}
	
	/**
	 * Retrieves all leave records of employees who report to the logged-in manager.
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @author krishna kumar
	 */
	public List<LeaveHistoryWrapper> getTeamsLeave() throws ServiceException {
		
		Long managerId = authenticationService.getAuthenticatedEmployee().getId();
		
	    logger.info("Fetching team leave history for manager ID: {}" + managerId);

	    List<LeaveHistoryWrapper> leaveHistoryWrappers = new ArrayList<>();

	    try {
	        Employee manager = employeeService.findById(managerId);
	        if (manager == null) {
	            logger.info("Manager with ID {} not found" + managerId);
	            throw new ServiceException("Manager not found");
	        }

	        logger.info("Manager found: {}" + manager.getFirstName());

	        List<LeaveHistory> teamLeaves = leaveHistoryRepository.findLeavesByManagerIdAndActive(manager.getId());

	        logger.info("Found {} leave records for team under manager ID: {}" + teamLeaves.size() + managerId);

	        for (LeaveHistory leaveHistory : teamLeaves) {
	            try {
	                LeaveHistoryWrapper wrapper = createHistoryWrapper(leaveHistory);
	                if (wrapper != null) {
	                leaveHistoryWrappers.add(wrapper);
	                }
	            } catch (Exception e) {
	                logger.info("Error wrapping leave history ID: {}. Skipping this entry." + leaveHistory.getId() + e);
	            }
	        }

	    } catch (ServiceException e) {
	        logger.info("Error fetching manager details for ID: {}" + managerId + e);
	        throw e;
	    } catch (Exception e) {
	        logger.info("Unexpected error occurred while fetching team leave history for manager ID: {}" + managerId + e);
	        throw new ServiceException("Error fetching team leave history", e);
	    }

	    logger.info("Successfully fetched {} leave history wrappers for manager ID: {}" + leaveHistoryWrappers.size() + managerId);
	    return leaveHistoryWrappers;
	}
	
	
	/**
	 * Gets all pending leave count on the basis of manager.
	 * 
	 * 
	 * @return count
	 * @throws ServiceException
	 * @author krishna kumar
	 */
	public int getTeamsLeaveCount() throws ServiceException {

		try {
			List<LeaveHistory> leaveHistories = leaveHistoryRepository
					.findLeavesByManagerIdAndActive(authenticationService.getAuthenticatedEmployee().getId());

			logger.info("Fetched {} leave records for the manager." + leaveHistories.size());
			
			int count = 0;
			for(LeaveHistory leave: leaveHistories) {
				if(leave.getStatus() == Status.PENDING)
					count++;
			}

			logger.info("Number of pending leave records: {}" + count);

			return count;

		} catch (Exception e) {
			logger.info("Error occurred while fetching or processing team leave records: {}" + e.getMessage() + e);
			throw new ServiceException("Unable to fetch team leave count", e);
		}
	}
	
}
