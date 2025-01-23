package com.ems.service;

import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ems.domain.Employee;
import com.ems.domain.LeaveHistory;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.wrappers.LeaveApproveWrapper;

@EnableScheduling
@Service
public class LeaveScheduler {

	private LeaveService leaveService;
	private LeaveAccountService leaveAccountService;
	private EmployeeService employeeService;

	@Autowired
	public LeaveScheduler(com.ems.service.LeaveAccountService leaveAccountService, LeaveService leaveService,
			EmployeeService employeeService) {
		this.leaveAccountService = leaveAccountService;
		this.leaveService = leaveService;
		this.employeeService = employeeService;
	}

	/**
	 * Scheduler for add leave account for each employee and it is executed on 1
	 * jan at 12 am.
	 * 
	 * @throws ServiceException
	 */
	@Scheduled(cron = "0 0 0 4 1 ? ")
	public void addEmployeeLeaveAccountPerYear() throws ServiceException {
		List<Employee> employees = employeeService.findByActive();
		for (Employee employee : employees) {
			leaveAccountService.addLeaveAccount(employee, DateUtil.getFinancialYear(new LocalDate()), true);
		}
	}

	/**
	 * this scheduler is executed at every day change.
	 * 
	 * @throws ServiceException
	 * @throws SerialException
	 */
	@Scheduled(cron = "0 0 0 1/1 * ? ")
	public void approvedExpireLeaveIfNotRejected() throws ServiceException, SerialException {
		List<LeaveHistory> leaveHistories = leaveService.getExpireLeaveIfNotRejected();
		LeaveApproveWrapper leaveApproveWrapper = new LeaveApproveWrapper();
		leaveApproveWrapper.setManagerComments("Auto Committed");
		leaveApproveWrapper.setIsApproved(true);
		for (LeaveHistory leaveHistory : leaveHistories) {
			leaveApproveWrapper.setLeaveId(leaveHistory.getId());
			leaveService.approveLeave(leaveApproveWrapper, true);
		}
	}

}
