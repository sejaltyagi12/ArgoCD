package com.ems.wrappers;

import com.ems.domain.Employee;
import com.ems.domain.LeaveHistory;

public class ExcelReportWrapper {
	
	private Employee employee;
	
	private LeaveHistory leave;
	
	private Employee manager;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LeaveHistory getLeave() {
		return leave;
	}

	public void setLeave(LeaveHistory leave) {
		this.leave = leave;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}
	
}
