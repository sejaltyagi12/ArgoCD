package com.ems.wrappers;

import java.util.List;


public class EmployeeChartWrapper {

	private List<ChartData> department;
	
	private List<ChartData> company;
	
	private List<ChartData> designation;

	public List<ChartData> getDepartment() {
		return department;
	}

	public void setDepartment(List<ChartData> department) {
		this.department = department;
	}

	public List<ChartData> getCompany() {
		return company;
	}

	public void setCompany(List<ChartData> company) {
		this.company = company;
	}

	public List<ChartData> getDesignation() {
		return designation;
	}

	public void setDesignation(List<ChartData> designation) {
		this.designation = designation;
	}

	public EmployeeChartWrapper(List<ChartData> department, List<ChartData> company, List<ChartData> designation) {
		this.department = department;
		this.company = company;
		this.designation = designation;
	}
	
	
	
}
