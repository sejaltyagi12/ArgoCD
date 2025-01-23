package com.ems.servicefinder.utils;

import javax.sql.rowset.serial.SerialException;

import com.ems.enums.LeaveName;

public enum PaySlipTags {

	WORKING_DAYS("No. of working days"), BASIC_SALARY("Basic Salary"), HRA("HRA"), MEDICAL(
			"Medical"), SPECIAL_ALLOWANCES("Special Allow."), LTA("LTA"), VEHICLE_RUNNING(
					"Vehicle Running"), ATTIRE_ALLOWANCES("Attire Allowance"), VEHICLE_REIMBURSE(
							"Vehical Reimbursement"), TELEPHONE_REIMBURSE("Tel. Reimbursement"), PF("PF Employer"), ESI(
									"ESI Employer"), DEDUCTION_TDS("Income Tax"), ABSENT("Absent"), DEDUCTION_LEAVE(
											"Leave"), DEDUCTION_OTHERS("Other Deductions"), ARREAR("Arrear"), TRANSPORT_ALLOWANCES("Transport Allowance"),LOAN("Loan");

	private final String name;

	private PaySlipTags(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public static PaySlipTags getEnum(String value) throws SerialException {
		for (PaySlipTags v : values()) {
			if (v.getName().equalsIgnoreCase(value)) {
				return v;
			}
		}
		return null;
	}
}
