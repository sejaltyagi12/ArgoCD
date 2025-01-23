package com.ems.wrappers;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ems.domain.LeaveHistory;
import com.ems.domain.LeaveType;
import com.ems.domain.LeaveHistory.Status;

public class LeaveHistoryWrapper {

	private Long id;

	private Long empId;

	private String empCode;

	private Date fromDate;

	private Date toDate;

	private Boolean isApproved;

	private LeaveType leaveType;

	private Date appliedDate;

	private Date leaveStatusDate;

	private Long approvedBy;

	private Float dayCount;

	private Integer deductableDayCount;

	private String employeeComments;

	private String employeeName;

	private String managerComments;

	private Boolean actionTaken;

	private Long managerId;

	private String attachmentUrl;
	
	private String deductionDetails;

	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING;

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Boolean getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(Boolean actionTaken) {
		this.actionTaken = actionTaken;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public Date getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
	}

	public Date getLeaveStatusDate() {
		return leaveStatusDate;
	}

	public void setLeaveStatusDate(Date leaveStatusDate) {
		this.leaveStatusDate = leaveStatusDate;
	}

	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Float getDayCount() {
		return dayCount;
	}

	public void setDayCount(Float dayCount) {
		this.dayCount = dayCount;
	}

	public Integer getDeductableDayCount() {
		return deductableDayCount;
	}

	public void setDeductableDayCount(Integer deductableDayCount) {
		this.deductableDayCount = deductableDayCount;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	/**
	 * @return the deductionDetails
	 */
	public String getDeductionDetails() {
		return deductionDetails;
	}

	/**
	 * @param deductionDetails the deductionDetails to set
	 */
	public void setDeductionDetails(String deductionDetails) {
		this.deductionDetails = deductionDetails;
	}

	public LeaveHistoryWrapper(LeaveHistory leaveHistory) {
		this.id = leaveHistory.getId();

		this.empId = leaveHistory.getEmpId();

		this.fromDate = leaveHistory.getFromDate();

		this.toDate = leaveHistory.getToDate();

		// this.isApproved = leaveHistory.getIsApproved();

		this.leaveType = leaveHistory.getLeaveType();

		this.appliedDate = leaveHistory.getAppliedDate();

		this.leaveStatusDate = leaveHistory.getApprovedDate();

		this.approvedBy = leaveHistory.getApprovedBy();

		this.dayCount = leaveHistory.getDayCount();

		this.deductableDayCount = leaveHistory.getDeductableDayCount();

		this.employeeComments = leaveHistory.getEmployeeComments();

		this.managerComments = leaveHistory.getManagerComments();

		// this.actionTaken = leaveHistory.getActionTaken();

		this.status = leaveHistory.getStatus();

		this.managerId = leaveHistory.getManagerId();

		this.attachmentUrl = leaveHistory.getAttachmentUrl();
	}

}
