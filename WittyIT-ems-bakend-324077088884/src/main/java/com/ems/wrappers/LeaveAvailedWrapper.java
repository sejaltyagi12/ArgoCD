package com.ems.wrappers;

import java.math.BigDecimal;

public class LeaveAvailedWrapper {
	private String month;
	private float availableCasualLeave;
	private float availablePrivelegeLeave;
	private float leaveAvailed;
	private float leaveWithOutPay;

	public LeaveAvailedWrapper() {
	}

	public LeaveAvailedWrapper(String month, Float availableCasualLeave, Float availablePrivelegeLeave) {
		this.month = month;
		this.availableCasualLeave = availableCasualLeave < 0 ? 0 : availableCasualLeave;
		this.availablePrivelegeLeave = availablePrivelegeLeave;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the availableCasualLeave
	 */
	public float getAvailableCasualLeave() {
		return new BigDecimal(availableCasualLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param availableCasualLeave
	 *            the availableCasualLeave to set
	 */
	public void setAvailableCasualLeave(float availableCasualLeave) {
		this.availableCasualLeave = availableCasualLeave;
	}

	/**
	 * @return the availablePrivelegeLeave
	 */
	public float getAvailablePrivelegeLeave() {
		return new BigDecimal(availablePrivelegeLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param availablePrivelegeLeave
	 *            the availablePrivelegeLeave to set
	 */
	public void setAvailablePrivelegeLeave(float availablePrivelegeLeave) {
		this.availablePrivelegeLeave = availablePrivelegeLeave;
	}

	/**
	 * @return the leaveAvailed
	 */
	public float getLeaveAvailed() {
		return new BigDecimal(leaveAvailed).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param leaveAvailed
	 *            the leaveAvailed to set
	 */
	public void setLeaveAvailed(float leaveAvailed) {
		this.leaveAvailed = leaveAvailed;
	}

	/**
	 * @return the leaveWithOutPay
	 */
	public float getLeaveWithOutPay() {
		return new BigDecimal(leaveWithOutPay).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param leaveWithOutPay
	 *            the leaveWithOutPay to set
	 */
	public void setLeaveWithOutPay(float leaveWithOutPay) {
		this.leaveWithOutPay = leaveWithOutPay;
	}

}
