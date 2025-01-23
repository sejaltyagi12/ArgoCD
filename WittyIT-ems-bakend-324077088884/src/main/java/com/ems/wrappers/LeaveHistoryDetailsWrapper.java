package com.ems.wrappers;

import java.math.BigDecimal;

public class LeaveHistoryDetailsWrapper {

	private float deductedCasualLeave;
	private float deductedPrivilegeLeave;
	private float deductedSickLeave;
	private float deductedLeaveWithOutPay;
	private float firstMonthLeaveWithOutPay;
	private int year;

	public LeaveHistoryDetailsWrapper() {

	}

	public LeaveHistoryDetailsWrapper(int year, float deductedCasualLeave, float deductedPrivilegeLeave,
			float deductedSickLeave, float deductedLeaveWithOutPay) {
		this.year = year;
		this.deductedCasualLeave = deductedCasualLeave;
		this.deductedPrivilegeLeave = deductedPrivilegeLeave;
		this.deductedSickLeave = deductedSickLeave;
		this.deductedLeaveWithOutPay = deductedLeaveWithOutPay;
	}

	public LeaveHistoryDetailsWrapper(int year) {
		this.year = year;
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
	 * @return the deductedCasualLeave
	 */
	public float getDeductedCasualLeave() {
		return new BigDecimal(deductedCasualLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
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
		return new BigDecimal(deductedPrivilegeLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
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
		return new BigDecimal(deductedSickLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
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
		return new BigDecimal(deductedLeaveWithOutPay).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param deductedLeaveWithOutPay
	 *            the deductedLeaveWithOutPay to set
	 */
	public void setDeductedLeaveWithOutPay(float deductedLeaveWithOutPay) {
		this.deductedLeaveWithOutPay = deductedLeaveWithOutPay;
	}

	public boolean isDeductedCasualOrSickLeave() {
		return deductedCasualLeave != 0 || deductedSickLeave != 0;
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
		return "LeaveHistoryDetailsWrapper [deductedCasualLeave=" + deductedCasualLeave + ", deductedPrivilegeLeave="
				+ deductedPrivilegeLeave + ", deductedSickLeave=" + deductedSickLeave + ", deductedLeaveWithOutPay="
				+ deductedLeaveWithOutPay + ", year=" + year + "]";
	}

}
