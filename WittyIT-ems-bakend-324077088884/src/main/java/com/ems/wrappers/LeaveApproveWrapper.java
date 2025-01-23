package com.ems.wrappers;


public class LeaveApproveWrapper {

	private String managerComments;

	private Long leaveId;
	
	private Boolean isApproved;

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}
	
	public Boolean isApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	
	
	
}
