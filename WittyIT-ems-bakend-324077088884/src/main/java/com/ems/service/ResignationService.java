package com.ems.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.configuration.PropConfig;
import com.ems.domain.Employee;
import com.ems.domain.Resignation;
import com.ems.domain.Resignation.Status;
import com.ems.domain.ResignationReason;
import com.ems.domain.ResignationType;
import com.ems.exception.ServiceException;
import com.ems.repository.ResignationReasonRepository;
import com.ems.repository.ResignationRepository;
import com.ems.repository.ResignationTypeRepository;
import com.ems.servicefinder.utils.Constants;
import com.ems.wrappers.ResignationWrapper;

@Service
@Transactional
public class ResignationService {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	ResignationRepository resignationRepository;

	@Autowired
	ResignationReasonRepository resignationReasonRepository;

	@Autowired
	ResignationTypeRepository resignationTypeRepository;

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	private PropConfig propConfig;

	@Autowired
	private EmailService emailService;

	@Autowired
	LeaveAccountService leaveAccountService;

	private static final Logger logger = Logger.getLogger(ResignationService.class.getName());

	/**
	 * Gets all Resignations Types
	 * 
	 * @return Resignation type list
	 * @throws ServiceException
	 */
	public List<ResignationType> getResignationTypes() throws ServiceException {
		return resignationTypeRepository.findAll();
	}

	/**
	 * Gets all Resignations Reasons
	 * 
	 * @return Resignation reason list
	 * @throws ServiceException
	 */
	public List<ResignationReason> getResignationReasons() throws ServiceException {
		return resignationReasonRepository.findAll();
	}

	/**
	 * Submit a Resignation | submit a resignation by HR
	 * 
	 * @param resignation
	 * @return Resignation
	 * @throws ServiceException
	 */
	public ResignationWrapper addResignation(Resignation resignation) throws ServiceException {

		DateTime today = new DateTime();
		Employee authEmployee = authenticationService.getAuthenticatedEmployee();

		if (!authEmployee.getRole().getRoleName().equalsIgnoreCase("employee")) {
			authEmployee = resignation.getEmployee();
		}

		Date employeeJoiningDate = authEmployee.getJoiningDate();
		Date employeeResignationDate = resignationRepository.findLastResignationDateByEmployeeId(authEmployee.getId());

		if (!resignationRepository
				.findByEmployeeAndManagerStatusOrHrStatus(authEmployee, Status.PENDING, Status.PENDING).isEmpty())
			throw new ServiceException("Sorry, Previous resignation is in process.");

		if (!resignationRepository.findByEmployeeAndHrStatus(authEmployee, Status.APPROVED).isEmpty()
				&& employeeJoiningDate.before(employeeResignationDate))
			throw new ServiceException("Sorry can't apply. Your previous resignation has been accepted.");

		if (resignation.getResignationText() == null)
			throw new ServiceException("Please add resignation text.");

		ResignationReason reason = resignationReasonRepository.findById(resignation.getReasonId());

		ResignationType type = resignationTypeRepository.findById(resignation.getTypeId());

		if (reason == null || type == null)
			throw new ServiceException("Please enter a valid reason and type.");

		resignation.setEmployee(authEmployee);
		resignation.setManagerId(authEmployee.getManagerId());
		resignation.setResignationDate(today);
		resignation.setLastDay(today.plusDays(authEmployee.getCompanyCategory().getNoticePeriod()));
		resignation.setReason(reason);
		resignation.setType(type);
		resignation = resignationRepository.save(resignation);
		if (propConfig.isIncludeResignationMail())
			emailService.sendApplyResignationMail(authEmployee, resignation, reason);

		return new ResignationWrapper(resignation);

	}

	/**
	 * Get current Resignation Deatil of Logged in User
	 * 
	 * @param resignation
	 * @return Resignation
	 * @throws ServiceException
	 */
	public List<ResignationWrapper> getCurrentResignation() throws ServiceException {
		
		logger.info("getting current resignation of login employee");

		Employee authEmployee = authenticationService.getAuthenticatedEmployee();

		List<Resignation> resigns = resignationRepository.findByEmployeeAndManagerStatusOrHrStatus(authEmployee,
				Status.PENDING, Status.PENDING);

		if (resigns.isEmpty())
			resigns = resignationRepository.findByEmployeeAndHrStatus(authEmployee, Status.APPROVED);
		List<ResignationWrapper> resignationWrappers = new ArrayList<>();
		for (Resignation resignation : resigns)
			resignationWrappers.add(postHandleNotes(resignation, authEmployee));
		
		logger.info("successfully get loged in user : {}" + authEmployee.getFirstName() +  " details ");
		
		return resignationWrappers;
	}

	/**
	 * Submit a Resignation
	 * 
	 * @param resignation
	 * @return Resignation
	 * @throws ServiceException
	 */
	public ResignationWrapper submitManagerApproval(Resignation resign) throws ServiceException {

		Resignation resignation = resignationRepository.findById(resign.getId());

		if (resignation != null) {
			if (!resignation.getEmployee().getManagerId()
					.equals(authenticationService.getAuthenticatedEmployee().getId()))
				throw new ServiceException("Sorry, You are not manager of this employee.");

			resignation.setManagerPublicNotes(resign.getManagerPublicNotes());
			resignation.setManagerPrivateNotes(resign.getManagerPrivateNotes());
			resignation.setManagerStatus(resign.isManagerAccepted() ? Status.APPROVED : Status.REJECTED);
			ResignationWrapper resignationWrapper = new ResignationWrapper(resignationRepository.save(resignation));

			resignationWrapper.setHrPrivateNotes(null);
			return resignationWrapper;
		}

		throw new ServiceException("Resignation not found.");
	}

	/**
	 * Submit a Resignation
	 * 
	 * @param resignation
	 * @return Resignation
	 * @throws ServiceException
	 */
	public ResignationWrapper submitHrApproval(Resignation resign) throws ServiceException {

		Resignation resignation = resignationRepository.findById(resign.getId());

		if (!(authenticationService.isAdmin() || authenticationService.isHR()))
			throw new ServiceException("Sorry, Only HR and Admin have access to this.");

		if (resignation != null) {
			resignation.setHrPublicNotes(resign.getHrPublicNotes());
			resignation.setHrPrivateNotes(resign.getHrPrivateNotes());
			resignation.setHrStatus(resign.isHrAccepted() ? Status.APPROVED : Status.REJECTED);
			resignation.setLastDay(resign.getLastDay());
			if (resignation.getManagerStatus().equals(Status.PENDING)) {
				resignation.setManagerStatus(resignation.getHrStatus());
				if (resignation.getEmployee().getId() == resignation.getManagerId()) {
					resignation.setManagerPublicNotes(resign.getHrPublicNotes());
					resignation.setManagerPrivateNotes(resign.getHrPrivateNotes());
				} else {
					resignation.setManagerPublicNotes("Based on HR feedback");
				}
			}
			return new ResignationWrapper(resignationRepository.save(resignation));
		}

		throw new ServiceException("Resignation not found.");
	}

	/**
	 * gets all Resignations by employee
	 * 
	 * @return Resignation List
	 * @throws ServiceException
	 */
	public Object getResignationList(Long id, Long empId) throws ServiceException {

		List<ResignationWrapper> resignationWrappers = new ArrayList<>();
		if (id != null) {
			Resignation resignation = resignationRepository.findById(id);

			if (resignation != null) {

				return postHandleNotes(resignation, authenticationService.getAuthenticatedEmployee());
			}

			throw new ServiceException("Resignation not found");
		}

		if (empId != null) {
			Employee employee = employeeService.findById(empId);

			if (employee == null)
				throw new ServiceException("Sorry, No employee found.");

			List<Resignation> resignations = resignationRepository.findByEmployee(employee);
			for (Resignation resignation : resignations)
				resignationWrappers.add(postHandleNotes(resignation, authenticationService.getAuthenticatedEmployee()));

			return resignationWrappers;
		}

		Employee employee = authenticationService.getAuthenticatedEmployee();
		int level = Integer.parseInt(employee.getDesignation().getLevel().split("_")[1]);
		List<Resignation> resignations = null;
		if (authenticationService.isAdmin() || authenticationService.isHR()) {
//			HR means all HR including admin hr.
			if (authenticationService.isHR()) {
				resignations = resignationRepository.findAllButHimselfWithLessLevel(
						level + Constants.MAXIMUM_LEVEL_UPTO_HR_CAN_APPROVE_LEAVE, employee.getId());
			} else {
//				This is for management department. They can approve resignation of their same level users as they are of the highest level 20.
				resignations = resignationRepository.findAllButHimselfWithLessLevel(level + 1, employee.getId());
			}
		} else {
			resignations = resignationRepository
					.findByManagerId(authenticationService.getAuthenticatedEmployee().getId());
		}

		for (Resignation resignation : resignations)
			resignationWrappers.add(postHandleNotes(resignation, authenticationService.getAuthenticatedEmployee()));

		return resignationWrappers;
	}

	/**
	 * Hides private notes on basis of manager/Hr
	 * 
	 * @param resignation
	 * @param employee
	 * @throws ServiceException
	 */
	private ResignationWrapper postHandleNotes(Resignation resign, Employee employee) throws ServiceException {
		ResignationWrapper resignationWrapper = new ResignationWrapper(resign);

		if (!(authenticationService.isHR(employee) || authenticationService.isAdmin(employee))) {
			resignationWrapper.setHrPrivateNotes(null);

			if (!resign.getManagerId().equals(employee.getId()))
				resignationWrapper.setManagerPrivateNotes(null);
		}
		return resignationWrapper;
	}

	/**
	 * Reactivate the employee and its leave account if its join in april of after
	 * april in new year
	 * 
	 * @param empCode
	 * @return boolean
	 * @throws ServiceException
	 */
	public String activateEmployee(String empCode, DateTime date) throws ServiceException {

		logger.info("Activating Employee empCode " + empCode);

		Employee employee = employeeService.findByEmpCode(empCode);

		if (employee != null) {

			employee.setIsActive(true);
			employee.setDeleted(false);
			employee.setJoiningDate(date);
			employeeService.saveOnly(employee);

			LocalDate currentDateWithYearAndMonth = LocalDate.now();
			LocalDate newYearLeavesFilledDate = currentDateWithYearAndMonth.withMonthOfYear(4).withDayOfMonth(1);

			Resignation resignation = resignationRepository.latestResignation(employee);

			LocalDate lastDateOfEmployee = resignation.getLastDay().toLocalDate();

			LocalDate reHireDate = LocalDate.now();

			if (reHireDate.isAfter(newYearLeavesFilledDate) && lastDateOfEmployee.isBefore(newYearLeavesFilledDate)) {

				leaveAccountService.resetLeaveAccount(employee);

			}
			logger.info("Employee Activated Successfully: {} " + "empName: " + employee.getFullName() + " "
					+ "empCode: " + empCode);
			return "true";
		}
		throw new ServiceException("Employee not found");
	}

}
