package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "company_category")
public class CompanyCategory {

	@Id
	@GeneratedValue
	private Integer companyId;

	@NotNull
	private String companyName;

	@NotNull
	private String companyPrefix;

	@NotNull
	private Integer totalSickLeave;

	@NotNull
	private Integer totalCasualLeave;

	@NotNull
	private Integer noticePeriod;

	@NotNull
	private Integer totalPrivilegeLeave;

	@NotNull
	private Integer totalMarriageLeave;

	@NotNull
	private Integer totalMaternityLeave;

	@NotNull
	private Integer totalPaternityLeave;

	@NotNull
	private Float totalCivilDuityLeave;

	@NotNull
	private Integer totalPrivilegeLeaveCarryForwarder;
	
	@NotNull
	private String unit;

	/**
	 * @return the noticePeriod
	 */
	public Integer getNoticePeriod() {
		return noticePeriod;
	}

	/**
	 * @param noticePeriod
	 *            the noticePeriod to set
	 */
	public void setNoticePeriod(Integer noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	/**
	 * Sets the Id.
	 *
	 * @param id
	 *            the id
	 * 
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets The Company Id.
	 *
	 * @return the Company Id
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the Company Name.
	 *
	 * @param id
	 *            the companyName
	 * 
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * Gets The Company Name.
	 *
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * Sets the Company Prefix.
	 *
	 * @param id
	 *            the Company Prefix
	 * 
	 */
	public void setCompanyPrefix(String companyPrefix) {
		this.companyPrefix = companyPrefix;
	}

	/**
	 * Gets The Company Prefix.
	 *
	 * @return the Company Prefix
	 */
	public String getCompanyPrefix() {
		return companyPrefix;
	}

	/**
	 * Gets The total sick leaves associated with a company.
	 *
	 * @return the total Sick Leave
	 */
	public Integer getTotalSickLeave() {
		return totalSickLeave;
	}

	/**
	 * Sets the total sick leaves associated with a company.
	 *
	 * @param id
	 *            the total sick Leave
	 * 
	 */
	public void setTotalSickLeave(Integer totalSickLeave) {
		this.totalSickLeave = totalSickLeave;
	}

	/**
	 * Gets The total casual leaves associated with a company.
	 *
	 * @return the total casual Leave
	 */
	public Integer getTotalCasualLeave() {
		return totalCasualLeave;
	}

	/**
	 * Sets the total casual leaves associated with a company.
	 *
	 * @param id
	 *            the total casual Leave
	 * 
	 */
	public void setTotalCasualLeave(Integer totalCasualLeave) {
		this.totalCasualLeave = totalCasualLeave;
	}

	/**
	 * @return the totalPrivilegeLeave
	 */
	public Integer getTotalPrivilegeLeave() {
		return totalPrivilegeLeave;
	}

	/**
	 * @param totalPrivilegeLeave
	 *            the totalPrivilegeLeave to set
	 */
	public void setTotalPrivilegeLeave(Integer totalPrivilegeLeave) {
		this.totalPrivilegeLeave = totalPrivilegeLeave;
	}

	/**
	 * @return the totalMarriageLeave
	 */
	public Integer getTotalMarriageLeave() {
		return totalMarriageLeave;
	}

	/**
	 * @param totalMarriageLeave
	 *            the totalMarriageLeave to set
	 */
	public void setTotalMarriageLeave(Integer totalMarriageLeave) {
		this.totalMarriageLeave = totalMarriageLeave;
	}

	/**
	 * @return the totalMaternityLeave
	 */
	public Integer getTotalMaternityLeave() {
		return totalMaternityLeave;
	}

	/**
	 * @param totalMaternityLeave
	 *            the totalMaternityLeave to set
	 */
	public void setTotalMaternityLeave(Integer totalMaternityLeave) {
		this.totalMaternityLeave = totalMaternityLeave;
	}

	/**
	 * @return the totalPaternityLeave
	 */
	public Integer getTotalPaternityLeave() {
		return totalPaternityLeave;
	}

	/**
	 * @param totalPaternityLeave
	 *            the totalPaternityLeave to set
	 */
	public void setTotalPaternityLeave(Integer totalPaternityLeave) {
		this.totalPaternityLeave = totalPaternityLeave;
	}

	/**
	 * @return the totalCivilDuityLeave
	 */
	public Float getTotalCivilDuityLeave() {
		return totalCivilDuityLeave;
	}

	/**
	 * @param totalCivilDuityLeave
	 *            the totalCivilDuityLeave to set
	 */
	public void setTotalCivilDuityLeave(Float totalCivilDuityLeave) {
		this.totalCivilDuityLeave = totalCivilDuityLeave;
	}

	/**
	 * @return the totalPrivilegeLeaveCarryForwarder
	 */
	public Integer getTotalPrivilegeLeaveCarryForwarder() {
		return totalPrivilegeLeaveCarryForwarder;
	}

	/**
	 * @param totalPrivilegeLeaveCarryForwarder
	 *            the totalPrivilegeLeaveCarryForwarder to set
	 */
	public void setTotalPrivilegeLeaveCarryForwarder(Integer totalPrivilegeLeaveCarryForwarder) {
		this.totalPrivilegeLeaveCarryForwarder = totalPrivilegeLeaveCarryForwarder;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

}
