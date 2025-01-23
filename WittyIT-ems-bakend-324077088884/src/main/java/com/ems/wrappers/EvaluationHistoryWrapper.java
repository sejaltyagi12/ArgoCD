package com.ems.wrappers;

import java.util.ArrayList;
import java.util.List;

import com.ems.domain.EvaluationHistory;

public class EvaluationHistoryWrapper 
{
	private Long employeeId;
	
	private String employeeName;
	
	private List<TargetListWrapper> categories = new ArrayList<>();

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	public EvaluationHistoryWrapper(Long employeeId, String employeeName) {
		this.employeeId = employeeId;
		this.employeeName = employeeName;
	}
	
	
	public List<TargetListWrapper> getCategories() {
		return categories;
	}

	public void setCategories(List<TargetListWrapper> categories) {
		this.categories = categories;
	}

	public EvaluationHistoryWrapper() {
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!(obj instanceof EvaluationHistoryWrapper))
	        return false;
	    else if (this.employeeId.equals(((EvaluationHistoryWrapper) obj).getEmployeeId())) 
 	        return true;
	    
	    if (!(obj instanceof EvaluationHistory))
	        return false;
	    else if (this.employeeId.equals(((EvaluationHistory) obj).getEmployee().getId())) 
 	        return true;
	    	    
	    if (this.employeeId == null || this.employeeName == null) {
	        return false;
	    }
	    
	    return false;
	}

	
}
