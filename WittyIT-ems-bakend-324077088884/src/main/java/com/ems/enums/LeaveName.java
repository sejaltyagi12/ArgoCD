package com.ems.enums;

import javax.sql.rowset.serial.SerialException;

public enum LeaveName {
	Casual_Leave("Casual Leave"), Sick_Leave("Sick Leave"), Civil_Duty_Leave("Civil Duty Leave"), Privilege_Leave(
			"Privilege Leave"), Marriage_Leave(
					"Marriage Leave"), Maternity_Leave("Maternity Leave"), Paternity_Leave("Paternity Leave");

	public String leaveName;

	private LeaveName(String leaveName) {
		this.leaveName = leaveName;
	}

	/**
	 * @return the leaveName
	 */
	public String getLeaveName() {
		return leaveName;
	}

	/**
	 * @param leaveName
	 *            the leaveName to set
	 */
	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}

	public static LeaveName getEnum(String value) throws SerialException {
		for (LeaveName v : values()) {
			if (v.getLeaveName().equalsIgnoreCase(value)) {
				return v;
			}
		}
		throw new SerialException("Invalid Leave Type.");
	}
}
