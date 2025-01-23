package com.ems.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.domain.Employee;
import com.ems.domain.LeaveHistory;
import com.ems.domain.LeaveHistory.Status;
import com.ems.domain.LeaveType;
import com.ems.enums.LeaveName;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.repository.HolidayRepository;
import com.ems.repository.LeaveHistoryRepository;
import com.ems.repository.LeaveTypesRepository;
import com.ems.servicefinder.utils.Constants;
import com.ems.servicefinder.utils.UserUtils;
import com.ems.wrappers.ApplyLeaveWrapper;

/**
 * This service is used for all leave specific or non specific validation.
 * Please go through the comments for more info.
 * 
 * @author Sarit Mukherjee
 *
 */
@Service
public class LeaveValidatorService {
	
	 private static final Logger logger = LoggerFactory.getLogger(LeaveValidatorService.class);

	@Autowired
	private LeaveTypesRepository leaveTypesRepository;

	@Autowired
	private LeaveHistoryRepository leaveHistoryRepository;
	
	@Autowired
	private HolidayService holidayService;

	/**
	 * It is not specific to leave types. Used for general validation. But leave
	 * specific methods should be called explicitly.
	 * 
	 * @param leaveWrapper
	 * @param employee
	 * @return
	 * @throws ServiceException
	 *             If unsuccessful
	 */
	public boolean validate(ApplyLeaveWrapper leaveWrapper, Employee employee) throws ServiceException {

		LeaveType leaveType = leaveTypesRepository.findByTypeId(leaveWrapper.getTypeId());

		// If the starting date of leave is prior of joining date.
		if (leaveWrapper.getFromDate().withTimeAtStartOfDay()
				.isBefore(new DateTime(employee.getJoiningDate()).withTimeAtStartOfDay())) {
			throw new ServiceException("Please choose some other date. You can not apply before joining date.");
		}

		// Error thrown for back date application for leaveType other then sick
		// and casual leave.
		if (!(leaveType.getType().equalsIgnoreCase(LeaveName.Sick_Leave.getLeaveName())
				|| leaveType.getType().equalsIgnoreCase(LeaveName.Casual_Leave.getLeaveName()))
				&& leaveWrapper.getFromDate().withTimeAtStartOfDay().isBefore(new DateTime().withTimeAtStartOfDay())) {
			throw new ServiceException("You can not apply for past date.");
		}

		// Back date leave application is available till 15th of last month for
		// Sick and Casual leave
//		if ((leaveType.getType().equalsIgnoreCase(LeaveName.Sick_Leave.getLeaveName())
//				|| leaveType.getType().equalsIgnoreCase(LeaveName.Casual_Leave.getLeaveName()))
//				&& leaveWrapper.getFromDate().withTimeAtStartOfDay()
//						.isBefore(new DateTime().minusMonths(1).withDayOfMonth(15).withTimeAtStartOfDay())) {
//			LocalDate lastMonthDate = new LocalDate().minusMonths(1).withDayOfMonth(15);
//			throw new ServiceException(
//					"Back date leave application is available till 15th of last month for Sick and Casual leave. That is till "
//							+ lastMonthDate + ".");
//		}

		// Checking if already have approved or pending application between the
		// starting and ending dates.
		List<LeaveHistory> leaveHistories = leaveHistoryRepository.findDuplicateEntry(employee.getId(),
				Arrays.asList(Status.APPROVED, Status.PENDING), leaveWrapper.getFromDate().toDate(),
				leaveWrapper.getToDate().toDate());

		if (leaveHistories.size() != 0) {
			throw new ServiceException("Please choose some other date. You have already applied for "
					+ DateUtil.removeTime(leaveHistories.get(0).fromDate()) + " to "
					+ DateUtil.removeTime(leaveHistories.get(0).toDate()));
		}
		return true;

	}

	/**
	 * Used to validate only privilege leave. Should be called explicitly.
	 * 
	 * @return true if successful. False if not this type of leave.
	 * @throws ServiceException
	 *             For unsuccessful
	 */
	public boolean validatePrivilegeLeave(ApplyLeaveWrapper leaveWrapper, Employee employee) throws ServiceException {
		LeaveType leaveType = leaveTypesRepository.findByTypeId(leaveWrapper.getTypeId());
		LocalDate startDate = new LocalDate(leaveWrapper.getFromDate());

		if (!leaveType.getType().equalsIgnoreCase(LeaveName.Privilege_Leave.getLeaveName()))
			return false;

		// // Privilege should be applied before 2 week.
		// int days = Days.daysBetween(new LocalDate(), startDate).getDays();
		// if (days < Constants.PRIVILEGE_LEAVE_DEADLINE_FOR_APPLY) {
		// throw new ServiceException("Privilege leave needs to be applied
		// before a minimum of "
		// + Constants.PRIVILEGE_LEAVE_DEADLINE_FOR_APPLY + " days.");
		// }

		// Minimum of 3 privilege leave should be taken at once.
		if (leaveWrapper.getDayCount() < Constants.MINIMUM_PRIVILEGE_LEAVE_AT_A_TIME) {
			throw new ServiceException("You can apply a minimum of " + Constants.MINIMUM_PRIVILEGE_LEAVE_AT_A_TIME
					+ " privilege leave at a time.");
		}

		// A maximum of 3 privilege leave can be taken in a financial year.
		LocalDate currentYearFirstDay = DateUtil.getFirstOrLastDateOfFinancialYear(startDate, true);
		LocalDate nextYearFirstDay = DateUtil.getFirstOrLastDateOfFinancialYear(startDate.plusYears(1), true);
		long approvedPL = leaveHistoryRepository.countByEmpIdAndLeaveTypeAndStatusAndToDateBetween(employee.getId(),
				leaveType, Status.APPROVED, currentYearFirstDay.toDate(), nextYearFirstDay.toDate());
		long pendingPL = leaveHistoryRepository.countByEmpIdAndLeaveTypeAndStatusAndToDateBetween(employee.getId(),
				leaveType, Status.PENDING, currentYearFirstDay.toDate(), nextYearFirstDay.toDate());
		long totalPLApplied = approvedPL + pendingPL;
		if (totalPLApplied >= Constants.MAXIMUM_TIMES_PRIVILEGE_LEAVE_TAKEN) {
			StringBuffer outputMessage = new StringBuffer(
					"Privilege leave can be applied for a maximum of " + Constants.MAXIMUM_TIMES_PRIVILEGE_LEAVE_TAKEN
							+ " times in an year. You already took " + approvedPL + ".");
			if (pendingPL > 0)
				outputMessage.append(" And we also have " + pendingPL + " pending privilege leave request.");
			throw new ServiceException(outputMessage.toString());
		}

		// Privilege leave is not applicable for your designation and you
		// haven't
		// complete your CONFORMATION_MONTHS
		boolean isEmployeeValidForPrivilegeLeave = (Integer
				.parseInt(employee.getDesignation().getLevel().split("_")[1]) > 0
				&& (UserUtils.isEmployeeJoiningGreaterThan(employee, startDate, Constants.CONFORMATION_MONTHS)));
		if (!isEmployeeValidForPrivilegeLeave) {
			throw new ServiceException("Privilege leave is not applicable for your designation :"
					+ employee.getDesignation() + ". And also you haven't complete your "
					+ Constants.CONFORMATION_MONTHS + " with " + employee.getCompanyCategory().getCompanyName());
		}
		return true;

	}

	/**
	 * Used to validate marriage leave. Should be called explicitly.
	 * 
	 * @return true if successful. False if not this type of leave.
	 * @throws ServiceException
	 *             For unsuccessful
	 */
	public boolean validateMarriageLeave(ApplyLeaveWrapper leaveWrapper, Employee employee) throws ServiceException {
		LeaveType leaveType = leaveTypesRepository.findByTypeId(leaveWrapper.getTypeId());

		if (!leaveType.getType().equalsIgnoreCase(LeaveName.Marriage_Leave.getLeaveName()))
			return false;

		// Employee can apply for marriage leave after one year of joining.
		Period yearDiff = new Period(new DateTime(employee.getJoiningDate()).withTimeAtStartOfDay(),
				leaveWrapper.getFromDate().withTimeAtStartOfDay());
		if (yearDiff.getYears() < Constants.MARRIAGE_LEAVE_YEAR_TERM) {
			throw new ServiceException("You can not apply for Marriage Leave. You need to complete atleast "
					+ Constants.MARRIAGE_LEAVE_YEAR_TERM + " year with "
					+ employee.getCompanyCategory().getCompanyName());
		}
		return true;

	}

	/**
	 * Used to validate sick leave. Should be called explicitly.
	 * 
	 * @return true if successful. False if not this type of leave.
	 * @throws ServiceException
	 *             For unsuccessful
	 */
	public boolean validateSickLeave(ApplyLeaveWrapper leaveWrapper, Employee employee) throws ServiceException {
		logger.info("inside validateSickLeave employee id : " + employee.getId());
		LeaveType leaveType = leaveTypesRepository.findByTypeId(leaveWrapper.getTypeId());

		if (!leaveType.getType().equalsIgnoreCase(LeaveName.Sick_Leave.getLeaveName()))
			return false;

		// It will print sick leave list of employee
		List<LeaveHistory> lastSickLeaveOfEmployeeList = leaveHistoryRepository
				.findLastSickLeaveOfEmployee(employee.getId(), 2);

		// check lastSickLeaveOfEmployeeList is empty or not
		if (!lastSickLeaveOfEmployeeList.isEmpty()) {

			logger.info("Last sick leave history is not empty");

			// sorting the list in descending order so we get recent sick leave on top
			Collections.sort(lastSickLeaveOfEmployeeList, new Comparator<LeaveHistory>() {

				@Override
				public int compare(LeaveHistory leave1, LeaveHistory leave2) {
					return leave2.getFromDate().compareTo(leave1.getFromDate());
				}
			});
			// mostRecentLeave is set by recent sick leave of employee
			LeaveHistory mostRecentLeave = lastSickLeaveOfEmployeeList.get(0);

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String leaveAppliedFromDate = dateFormat.format(mostRecentLeave.getFromDate());
			String leaveAppliedToDate = dateFormat.format(mostRecentLeave.getToDate());

			// lastToLastLeave is set by previous leave of recent leave
			LeaveHistory lastToLastLeave = (lastSickLeaveOfEmployeeList.size() > 1) ? lastSickLeaveOfEmployeeList.get(1)
					: null;
			//check mostRecentLeave status of employee is approved or pending 
			if (mostRecentLeave.getStatus().equals(Status.APPROVED)
					|| mostRecentLeave.getStatus().equals(Status.PENDING)) {

				logger.info("Details {} : {} : {} : {}", mostRecentLeave, mostRecentLeave.toDate().plusDays(1),
						leaveWrapper.getFromDate(), mostRecentLeave.getDayCount());

				// It will check if user is already applied for 2 days sick leave and try to
				// apply 1 day more leave in continuity then he/she will see error and if he
				// applied 1 day sick leave and then 1 day sick leave again and then again
				// he/she tried to aaply sick leave he/she will see exception if he/she not
				// uploaded medical certificate.
				if ((mostRecentLeave.getDayCount().equals(2F)
						&& mostRecentLeave.toDate().plusDays(1).equals(leaveWrapper.getFromDate())
						&& leaveWrapper.getFileContent() == null)
						|| (lastToLastLeave != null && lastToLastLeave.getDayCount().equals(1F)
								&& lastToLastLeave.toDate().equals(leaveWrapper.getFromDate().minusDays(2))
								&& leaveWrapper.getFileContent() == null)) {
					logger.info("inside days case");
					throw new ServiceException(
							"You need to upload a medical certificate; it is mandatory to do so when you have taken two sick leaves "
									+ leaveAppliedFromDate + " to " + leaveAppliedToDate);
				}
				if (mostRecentLeave.getDayCount().equals(1F)
						&& mostRecentLeave.toDate().plusDays(1).equals(leaveWrapper.getFromDate())
						&& leaveWrapper.getDayCount().equals(2F) && leaveWrapper.getFileContent() == null) {
					throw new ServiceException(
							"You need to upload a medical certificate; it is mandatory to do so when you have taken two sick leaves "
									+ leaveAppliedFromDate + " to " + leaveAppliedToDate);
				}

				DateTime previousDay = leaveWrapper.getFromDate().minusDays(1);

				// It will check if user applied for leave on monday then on friday he is
				// already on sick leave or not if it is then he/she will see exception
				if (previousDay.getDayOfWeek() == DateTimeConstants.SUNDAY) {
					logger.info("Inside Saturday Sunday case");
					if (mostRecentLeave.toDate().equals(leaveWrapper.getFromDate().minusDays(3))
							&& leaveWrapper.getFileContent() == null) {
						throw new ServiceException(
								"Please upload your Medical Certificate first because you are already on sick leave from "
										+ leaveAppliedFromDate + " to " + leaveAppliedToDate);
					}
				}
			}
		}
		// check holiday between user applied leave fromDate or toDate
		boolean holiday = holidayService.checkIsHolidayOnFromDateOrToDate(leaveWrapper.getFromDate().plusDays(1),
				leaveWrapper.getToDate().minusDays(1));
		logger.info("value of holiday inside validateSickLeave " + holiday);
		if (holiday && leaveWrapper.getDayCount().equals(3F)) {
			logger.info("checking is any holiday between start date or end date " + holiday);
//			leaveWrapper.setDayCount(2F);
			return true;
		}

		// Asking for medical certificate if applying for more then 2 days in
		// case of Sick leave.
		if (leaveWrapper.getDayCount() > Constants.Medical_TIME && leaveWrapper.getFileContent() == null) {
			throw new ServiceException("Please upload your Medical Certificate.");
		}
		return true;

	}
	/**
	 * Used to validate max 2 days causal leave can taken at a time. Should be called explicitly.
	 * @param leaveWrapper
	 * @param employee
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateCasualLeave(ApplyLeaveWrapper leaveWrapper,Employee employee) throws ServiceException{
		logger.info("validating causal leave of employee {}",employee.getFirstName());
		LeaveType leaveType = leaveTypesRepository.findByTypeId(leaveWrapper.getTypeId());

		if (!leaveType.getType().equalsIgnoreCase(LeaveName.Casual_Leave.getLeaveName()))
			return false;
		
		// check holiday between user applied leave fromDate or toDate
		boolean holiday = holidayService.checkIsHolidayOnFromDateOrToDate(leaveWrapper.getFromDate().plusDays(1),
				leaveWrapper.getToDate().minusDays(1));
		logger.info("value of holiday inside validateCasualLeave " + holiday);
		if (holiday && leaveWrapper.getDayCount().equals(3F)) {
			logger.info("checking is any holiday between start date or end date " + holiday);
//			leaveWrapper.setDayCount(2F);
			return true;
		}

		// check employee will not apply for more than 2 days leave as causal leave
		if (leaveWrapper.getDayCount() > Constants.MAX_CASUAL_LEAVE_TAKEN_AT_A_TIME ) {
			throw new ServiceException("As per the leave policy, you can't apply for more than two leaves in CL.");
		}
		logger.info("validate causal leave of employee {} successfully",employee.getFirstName());
		return true;
	}
}
