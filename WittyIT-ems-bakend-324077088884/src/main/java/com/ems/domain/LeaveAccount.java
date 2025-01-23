package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "leave_account")
public class LeaveAccount {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Long empId;

	@NotNull
	private Integer year;

	@NotNull
	private Float availedCasualLeave;

	@NotNull
	private Float availedSickLeave;

	@NotNull
	private Float availedPrivilegeLeave;

	@NotNull
	private Float totalCasualLeave;

	@NotNull
	private Float totalSickLeave;

	@NotNull
	private Float totalPrivilegeLeave;

	@LastModifiedDate
	private DateTime lastModified;

	@CreatedDate
	private DateTime creationDate;

	/**
	 * Gets The Leave Id.
	 *
	 * @return the Leave Id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the Leave Id.
	 *
	 * @param id
	 *            the Leave Id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets The Employee Id associated with a leave account.
	 *
	 * @return the Employee Id
	 */
	public Long getEmpId() {
		return empId;
	}

	/**
	 * Sets the Employee Id to a leave account.
	 *
	 * @param empId
	 *            the Employee Id
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	/**
	 * Gets The Leave account Year.
	 *
	 * @return the Leave account year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * Sets the Leave account Year.
	 *
	 * @param year
	 *            the year
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the availedCasualLeave
	 */
	public Float getAvailedCasualLeave() {
		return availedCasualLeave;
	}

	/**
	 * @param availedCasualLeave
	 *            the availedCasualLeave to set
	 */
	public void setAvailedCasualLeave(Float availedCasualLeave) {
		this.availedCasualLeave = availedCasualLeave;
	}

	/**
	 * @return the availedSickLeave
	 */
	public Float getAvailedSickLeave() {
		return availedSickLeave;
	}

	/**
	 * @param availedSickLeave
	 *            the availedSickLeave to set
	 */
	public void setAvailedSickLeave(Float availedSickLeave) {
		this.availedSickLeave = availedSickLeave;
	}

	/**
	 * @return the availedPrivilegeLeave
	 */
	public Float getAvailedPrivilegeLeave() {
		return availedPrivilegeLeave;
	}

	/**
	 * @param availedPrivilegeLeave
	 *            the availedPrivilegeLeave to set
	 */
	public void setAvailedPrivilegeLeave(Float availedPrivilegeLeave) {
		this.availedPrivilegeLeave = availedPrivilegeLeave;
	}

	/**
	 * @return the totalCasualLeave
	 */
	public Float getTotalCasualLeave() {
		return totalCasualLeave;
	}

	/**
	 * @param totalCasualLeave
	 *            the totalCasualLeave to set
	 */
	public void setTotalCasualLeave(Float totalCasualLeave) {
		this.totalCasualLeave = totalCasualLeave;
	}

	/**
	 * @return the totalSickLeave
	 */
	public Float getTotalSickLeave() {
		return totalSickLeave;
	}

	/**
	 * @param totalSickLeave
	 *            the totalSickLeave to set
	 */
	public void setTotalSickLeave(Float totalSickLeave) {
		this.totalSickLeave = totalSickLeave;
	}

	/**
	 * @return the totalPrivilegeLeave
	 */
	public Float getTotalPrivilegeLeave() {
		return totalPrivilegeLeave;
	}

	/**
	 * @param totalPrivilegeLeave
	 *            the totalPrivilegeLeave to set
	 */
	public void setTotalPrivilegeLeave(Float totalPrivilegeLeave) {
		this.totalPrivilegeLeave = totalPrivilegeLeave;
	}
	
	/**
	 * @return the lastModified
	 */
	public DateTime getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(DateTime lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the creationDate
	 */
	public DateTime getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LeaveAccount(Long empId, Integer year, Float availedCasualLeave, Float availedSickLeave,
			Float availedPrivilegeLeave, Float totalCasualLeave, Float totalSickLeave, Float totalPrivilegeLeave) {
		this.empId = empId;
		this.year = year;
		this.availedCasualLeave = availedCasualLeave;
		this.availedSickLeave = availedSickLeave;
		this.availedPrivilegeLeave = availedPrivilegeLeave;
		this.totalCasualLeave = totalCasualLeave;
		this.totalSickLeave = totalSickLeave;
		this.totalPrivilegeLeave = totalPrivilegeLeave;
	}

	public LeaveAccount() {

	}

}
