package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class PayrollVariable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="emp_id" , nullable=false)
	private Employee employee;
	
	private Integer month;
	
	private Integer year;
	
	private Float taxes;
	
	private Float bonus;
	
	private Float arrear;
	
	private Float otherDeductions;
	
	private Float otherAllowances;
	
	@JsonIgnore
	@LastModifiedDate
	private DateTime modifiedDate;
	
	@JsonIgnore
	@LastModifiedBy
	private Long modifiedBy;
	
	@JsonIgnore
	@CreatedDate
	private DateTime creationDate;
	
	@JsonIgnore
	@CreatedBy
	private Long createdBy;
	
	@Transient
	private Long empId;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the taxes
	 */
	public Float getTaxes() {
		return taxes;
	}

	/**
	 * @param taxes the taxes to set
	 */
	public void setTaxes(Float taxes) {
		this.taxes = taxes;
	}

	/**
	 * @return the bonus
	 */
	public Float getBonus() {
		return bonus;
	}

	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(Float bonus) {
		this.bonus = bonus;
	}

	/**
	 * @return the arrear
	 */
	public Float getArrear() {
		return arrear;
	}

	/**
	 * @param arrear the arrear to set
	 */
	public void setArrear(Float arrear) {
		this.arrear = arrear;
	}

	/**
	 * @return the otherDeductions
	 */
	public Float getOtherDeductions() {
		return otherDeductions;
	}

	/**
	 * @param otherDeductions the otherDeductions to set
	 */
	public void setOtherDeductions(Float otherDeductions) {
		this.otherDeductions = otherDeductions;
	}

	/**
	 * @return the otherAllowances
	 */
	public Float getOtherAllowances() {
		return otherAllowances;
	}

	/**
	 * @param otherAllowances the otherAllowances to set
	 */
	public void setOtherAllowances(Float otherAllowances) {
		this.otherAllowances = otherAllowances;
	}

	/**
	 * @return the modifiedDate
	 */
	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public Long getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
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

	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the empId
	 */
	public Long getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	
}
