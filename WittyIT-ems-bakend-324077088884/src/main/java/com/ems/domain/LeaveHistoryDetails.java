package com.ems.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ems.wrappers.LeaveHistoryDetailsWrapper;

@Entity
@Table(name = "leave_history_details")
public class LeaveHistoryDetails {

	@Id
	@GeneratedValue
	@Column(name = "leave_history_detail_id")
	private Long id;

	private int year;
	private float deductedCasualLeave;
	private float deductedPrivilegeLeave;
	private float deductedSickLeave;
	private float deductedLeaveWithOutPay;
	private float firstMonthLeaveWithOutPay;

	public LeaveHistoryDetails() {

	}

	public LeaveHistoryDetails(int year, float deductedCasualLeave, float deductedPrivilegeLeave,
			float deductedSickLeave, float deductedLeaveWithOutPay) {
		this.year = year;
		this.deductedCasualLeave = deductedCasualLeave;
		this.deductedPrivilegeLeave = deductedPrivilegeLeave;
		this.deductedSickLeave = deductedSickLeave;
		this.deductedLeaveWithOutPay = deductedLeaveWithOutPay;
	}

	public LeaveHistoryDetails(LeaveHistoryDetailsWrapper leaveHistoryDetailsWrapper) {
		this.year = leaveHistoryDetailsWrapper.getYear();
		this.deductedCasualLeave = leaveHistoryDetailsWrapper.getDeductedCasualLeave();
		this.deductedPrivilegeLeave = leaveHistoryDetailsWrapper.getDeductedPrivilegeLeave();
		this.deductedSickLeave = leaveHistoryDetailsWrapper.getDeductedSickLeave();
		this.deductedLeaveWithOutPay = leaveHistoryDetailsWrapper.getDeductedLeaveWithOutPay();
		this.firstMonthLeaveWithOutPay = leaveHistoryDetailsWrapper.getFirstMonthLeaveWithOutPay();
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the deductedCasualLeave
	 */
	public float getDeductedCasualLeave() {
		return deductedCasualLeave;
	}

	/**
	 * @param deductedCasualLeave
	 *            the deductedCasualLeave to set
	 */
	public void setDeductedCasualLeave(float deductedCasualLeave) {
		this.deductedCasualLeave = deductedCasualLeave;
	}

	/**
	 * @return the deductedPrivilegeLeave
	 */
	public float getDeductedPrivilegeLeave() {
		return deductedPrivilegeLeave;
	}

	/**
	 * @param deductedPrivilegeLeave
	 *            the deductedPrivilegeLeave to set
	 */
	public void setDeductedPrivilegeLeave(float deductedPrivilegeLeave) {
		this.deductedPrivilegeLeave = deductedPrivilegeLeave;
	}

	/**
	 * @return the deductedSickLeave
	 */
	public float getDeductedSickLeave() {
		return deductedSickLeave;
	}

	/**
	 * @param deductedSickLeave
	 *            the deductedSickLeave to set
	 */
	public void setDeductedSickLeave(float deductedSickLeave) {
		this.deductedSickLeave = deductedSickLeave;
	}

	/**
	 * @return the deductedLeaveWithOutPay
	 */
	public float getDeductedLeaveWithOutPay() {
		return deductedLeaveWithOutPay;
	}

	/**
	 * @param deductedLeaveWithOutPay
	 *            the deductedLeaveWithOutPay to set
	 */
	public void setDeductedLeaveWithOutPay(float deductedLeaveWithOutPay) {
		this.deductedLeaveWithOutPay = deductedLeaveWithOutPay;
	}

	/**
	 * @return the firstMonthLeaveWithOutPay
	 */
	public float getFirstMonthLeaveWithOutPay() {
		return firstMonthLeaveWithOutPay;
	}

	/**
	 * @param firstMonthLeaveWithOutPay the firstMonthLeaveWithOutPay to set
	 */
	public void setFirstMonthLeaveWithOutPay(float firstMonthLeaveWithOutPay) {
		this.firstMonthLeaveWithOutPay = firstMonthLeaveWithOutPay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LeaveHistoryDetails [id=" + id + ", deductedCasualLeave=" + deductedCasualLeave
				+ ", deductedPrivilegeLeave=" + deductedPrivilegeLeave + ", deductedSickLeave=" + deductedSickLeave
				+ ", deductedLeaveWithOutPay=" + deductedLeaveWithOutPay + "]";
	}

}
