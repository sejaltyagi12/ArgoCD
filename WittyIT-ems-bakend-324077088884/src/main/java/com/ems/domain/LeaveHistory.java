package com.ems.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

@Entity
@Table(name = "leave_history")
public class LeaveHistory {

	/**
	 * LeaveHistory.Status describes the possibilities for a leave status.
	 * Possible types: APPROVED, PENDING, WITHDRAWN, REJECTED
	 * 
	 * @author Avinash Tyagi
	 */
	public enum Status {
		APPROVED("Approved"), PENDING("Pending"), WITHDRAWN("Withdrawn"), REJECTED("Rejected"), REVOKED("Revoked");

		String stringValue;

		private Status(String stringValue) {
			this.stringValue = stringValue;
		}

		/**
		 * Gets the string value.
		 *
		 * @return the string value
		 */
		public String getStringValue() {
			return this.stringValue;
		}

		public String toString() {
			return this.stringValue;
		}

		public static Status getEnum(String value) {
			for (Status v : values()) {
				if (v.getStringValue().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new IllegalArgumentException();
		}
	}

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Long empId;

	@NotNull
	private Date fromDate;

	@NotNull
	private Date toDate;

	// @NotNull
	// private Boolean isApproved = false;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", nullable = false)
	private LeaveType leaveType;

	@NotNull
	private Date appliedDate = new Date();

	private Date approvedDate;

	private Long approvedBy;

	@NotNull
	private Float dayCount = 0.0f;

	@NotNull
	private Integer deductableDayCount = 0;

	private String employeeComments;

	private String managerComments;

	// @NotNull
	// private Boolean actionTaken = false;

	@NotNull
	private Long managerId;

	private String attachmentUrl;

	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING;
	
	private DateTime rejectedDate;
	
	private DateTime withdrawnDate;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "leave_history_detail", nullable = true)
	private List<LeaveHistoryDetails> leaveHistoryDetails;

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

	public DateTime fromDate() {
		return new DateTime(fromDate);
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public DateTime toDate() {
		return new DateTime(toDate);
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	// public Boolean getActionTaken() {
	// return actionTaken;
	// }
	//
	// public void setActionTaken(Boolean actionTaken) {
	// this.actionTaken = actionTaken;
	// }

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the rejectedDate
	 */
	public DateTime getRejectedDate() {
		return rejectedDate;
	}

	/**
	 * @param rejectedDate the rejectedDate to set
	 */
	public void setRejectedDate(DateTime rejectedDate) {
		this.rejectedDate = rejectedDate;
	}

	/**
	 * @return the withdrawnDate
	 */
	public DateTime getWithdrawnDate() {
		return withdrawnDate;
	}

	/**
	 * @param withdrawnDate the withdrawnDate to set
	 */
	public void setWithdrawnDate(DateTime withdrawnDate) {
		this.withdrawnDate = withdrawnDate;
	}

	/**
	 * @return the leaveHistoryDetails
	 */
	public List<LeaveHistoryDetails> getLeaveHistoryDetails() {
		return leaveHistoryDetails;
	}

	/**
	 * @param leaveHistoryDetails
	 *            the leaveHistoryDetails to set
	 */
	public void setLeaveHistoryDetails(List<LeaveHistoryDetails> leaveHistoryDetails) {
		this.leaveHistoryDetails = leaveHistoryDetails;
	}

	public LeaveHistory() {
	}

	public LeaveHistory(Long empId, Date fromDate, Date toDate, Float dayCount, String employeeComments, Long managerId,
			LeaveType leaveType) {
		this.empId = empId;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.dayCount = dayCount;
		this.employeeComments = employeeComments;
		this.managerId = managerId;
		this.leaveType = leaveType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LeaveHistory [id=" + id + ", empId=" + empId + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", status=" + status + "]";
	}

}