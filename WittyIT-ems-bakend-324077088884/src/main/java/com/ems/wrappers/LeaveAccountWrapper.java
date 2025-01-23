package com.ems.wrappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ems.domain.CompanyCategory;
import com.ems.domain.Employee;

public class LeaveAccountWrapper {

	private Float totalSickLeaves;
	private Float totalCasualLeaves;
	private Float totalPrivilegeLeave;
	private Float remainingCivilDutyLeave;
	private Integer remainingMarriageLeave;
	private Integer remainingMaternityLeave;
	private Integer remainingPaternityLeave;

	private Float remainingSickLeaves;
	private Float remainingCasualLeaves;
	private Float remainingPrivilegeLeave;

	private String unit;
	private List<LeaveAvailedWrapper> leaveAvailedWrappers;

	public LeaveAccountWrapper() {
	}

	public LeaveAccountWrapper(Float totalSickLeaves, Float totalCasualLeaves, Float totalPrivilegeLeave,
			Float remainingSickLeaves, Float remainingCasualLeaves, Float remainingPrivilegeLeave,
			CompanyCategory companyCategory, boolean isValidForPrivelege) {
		this.totalSickLeaves = totalSickLeaves;
		this.totalCasualLeaves = totalCasualLeaves;
		this.totalPrivilegeLeave = totalPrivilegeLeave;

		this.remainingSickLeaves = totalSickLeaves - remainingSickLeaves;
		this.remainingCasualLeaves = totalCasualLeaves - remainingCasualLeaves;
		this.remainingPrivilegeLeave = isValidForPrivelege ? (totalPrivilegeLeave - remainingPrivilegeLeave) : 0;
		this.remainingCivilDutyLeave = companyCategory.getTotalCivilDuityLeave();
		this.remainingMarriageLeave = companyCategory.getTotalMarriageLeave();
		this.remainingMaternityLeave = companyCategory.getTotalMaternityLeave();
		this.remainingPaternityLeave = companyCategory.getTotalPaternityLeave();

		this.unit = companyCategory.getUnit().substring(0, companyCategory.getUnit().length() - 1) + "("
				+ companyCategory.getUnit().substring(companyCategory.getUnit().length() - 1,
						companyCategory.getUnit().length())
				+ ")";
		leaveAvailedWrappers = new ArrayList<LeaveAvailedWrapper>();
	}

	public Float getTotalSickLeaves() {
		return new BigDecimal(totalSickLeaves).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setTotalSickLeaves(Float totalSickLeaves) {
		this.totalSickLeaves = totalSickLeaves;
	}

	public Float getRemainingSickLeaves() {
		return new BigDecimal(remainingSickLeaves).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setRemainingSickLeaves(Float remainingSickLeaves) {
		this.remainingSickLeaves = remainingSickLeaves;
	}

	public Float getTotalCasualLeaves() {
		return new BigDecimal(totalCasualLeaves).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setTotalCasualLeaves(Float totalCasualLeaves) {
		this.totalCasualLeaves = totalCasualLeaves;
	}

	public Float getRemainingCasualLeaves() {
		return new BigDecimal(remainingCasualLeaves).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public void setRemainingCasualLeaves(Float remainingCasualLeaves) {
		this.remainingCasualLeaves = remainingCasualLeaves;
	}

	/**
	 * @return the totalPrivilegeLeave
	 */
	public Float getTotalPrivilegeLeave() {
		return new BigDecimal(totalPrivilegeLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param totalPrivilegeLeave
	 *            the totalPrivilegeLeave to set
	 */
	public void setTotalPrivilegeLeave(Float totalPrivilegeLeave) {
		this.totalPrivilegeLeave = totalPrivilegeLeave;
	}

	/**
	 * @return the remainingPrivilegeLeave
	 */
	public Float getRemainingPrivilegeLeave() {
		return new BigDecimal(remainingPrivilegeLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param remainingPrivilegeLeave
	 *            the remainingPrivilegeLeave to set
	 */
	public void setRemainingPrivilegeLeave(Float remainingPrivilegeLeave) {
		this.remainingPrivilegeLeave = remainingPrivilegeLeave;
	}

	/**
	 * @return the remainingCivilDutyLeave
	 */
	public Float getRemainingCivilDutyLeave() {
		return new BigDecimal(remainingCivilDutyLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @param remainingCivilDutyLeave
	 *            the remainingCivilDutyLeave to set
	 */
	public void setRemainingCivilDutyLeave(Float remainingCivilDutyLeave) {
		this.remainingCivilDutyLeave = new BigDecimal(remainingCivilDutyLeave).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * @return the remainingMarriageLeave
	 */
	public Integer getRemainingMarriageLeave() {
		return remainingMarriageLeave;
	}

	/**
	 * @param remainingMarriageLeave
	 *            the remainingMarriageLeave to set
	 */
	public void setRemainingMarriageLeave(Integer remainingMarriageLeave) {
		this.remainingMarriageLeave = remainingMarriageLeave;
	}

	/**
	 * @return the remainingMaternityLeave
	 */
	public Integer getRemainingMaternityLeave() {
		return remainingMaternityLeave;
	}

	/**
	 * @param remainingMaternityLeave
	 *            the remainingMaternityLeave to set
	 */
	public void setRemainingMaternityLeave(Integer remainingMaternityLeave) {
		this.remainingMaternityLeave = remainingMaternityLeave;
	}

	/**
	 * @return the remainingPaternityLeave
	 */
	public Integer getRemainingPaternityLeave() {
		return remainingPaternityLeave;
	}

	/**
	 * @param remainingPaternityLeave
	 *            the remainingPaternityLeave to set
	 */
	public void setRemainingPaternityLeave(Integer remainingPaternityLeave) {
		this.remainingPaternityLeave = remainingPaternityLeave;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the leaveAvailedWrappers
	 */
	public List<LeaveAvailedWrapper> getLeaveAvailedWrappers() {
		return leaveAvailedWrappers;
	}

	/**
	 * @param leaveAvailedWrappers
	 *            the leaveAvailedWrappers to set
	 */
	public void setLeaveAvailedWrappers(List<LeaveAvailedWrapper> leaveAvailedWrappers) {
		this.leaveAvailedWrappers = leaveAvailedWrappers;
	}

}
