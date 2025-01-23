package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class PayrollFixed {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@NotNull
	@ManyToOne
	@JoinColumn(name="emp_id" , nullable=false)
	private Employee employee;
	
	@NotNull
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime validFrom;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime expiry;
	
	private Float basicPay;
	
	private Float hra;
	
	private Float medicalReimbursement;
	
	private Float specialAllowance;
	
	private Float businessPromotion;
	
	private Float lta;
	
	private Float vehicleReimbursement;
	
	private Float attireAllowance;
	
	private Float driverAllowance;
	
	private Float fixedBonus;
	
	private Float medicalInsurance;
	
	private Float employerPf;
	
	private Float employeePf;
	
	private Boolean isPfInPercent = false;
	
	private Float taxes;
	
	private Float vehicleRunning;
	
	private Float telReimbursement;
	
	private Float employerEsi;
	
	private Float employeeEsi;
	
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
	
	@NotNull
	private Boolean active;
	
	@NotNull
	private Boolean deleted = false;
		
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
	 * @return the validFrom
	 */
	public DateTime getValidFrom() {
		return validFrom;
	}

	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(DateTime validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * @return the expiry
	 */
	public DateTime getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(DateTime expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return the basicPay
	 */
	public Float getBasicPay() {
		return basicPay;
	}

	/**
	 * @param basicPay the basicPay to set
	 */
	public void setBasicPay(Float basicPay) {
		this.basicPay = basicPay;
	}

	/**
	 * @return the hra
	 */
	public Float getHra() {
		return hra;
	}

	/**
	 * @param hra the hra to set
	 */
	public void setHra(Float hra) {
		this.hra = hra;
	}

	/**
	 * @return the medicalReimbursement
	 */
	public Float getMedicalReimbursement() {
		return medicalReimbursement;
	}

	/**
	 * @param medicalReimbursement the medicalReimbursement to set
	 */
	public void setMedicalReimbursement(Float medicalReimbursement) {
		this.medicalReimbursement = medicalReimbursement;
	}

	/**
	 * @return the specialAllowance
	 */
	public Float getSpecialAllowance() {
		return specialAllowance;
	}

	/**
	 * @param specialAllowance the specialAllowance to set
	 */
	public void setSpecialAllowance(Float specialAllowance) {
		this.specialAllowance = specialAllowance;
	}

	/**
	 * @return the businessPromotion
	 */
	public Float getBusinessPromotion() {
		return businessPromotion;
	}

	/**
	 * @param businessPromotion the businessPromotion to set
	 */
	public void setBusinessPromotion(Float businessPromotion) {
		this.businessPromotion = businessPromotion;
	}

	/**
	 * @return the lta
	 */
	public Float getLta() {
		return lta;
	}

	/**
	 * @param lta the lta to set
	 */
	public void setLta(Float lta) {
		this.lta = lta;
	}

	/**
	 * @return the vehicleReimbursement
	 */
	public Float getVehicleReimbursement() {
		return vehicleReimbursement;
	}

	/**
	 * @param vehicleReimbursement the vehicleReimbursement to set
	 */
	public void setVehicleReimbursement(Float vehicleReimbursement) {
		this.vehicleReimbursement = vehicleReimbursement;
	}

	

	/**
	 * @return the attireAllowance
	 */
	public Float getAttireAllowance() {
		return attireAllowance;
	}

	/**
	 * @param attireAllowance the attireAllowance to set
	 */
	public void setAttireAllowance(Float attireAllowance) {
		this.attireAllowance = attireAllowance;
	}

	/**
	 * @return the driverAllowance
	 */
	public Float getDriverAllowance() {
		return driverAllowance;
	}

	/**
	 * @param driverAllowance the driverAllowance to set
	 */
	public void setDriverAllowance(Float driverAllowance) {
		this.driverAllowance = driverAllowance;
	}

	/**
	 * @return the fixedBonus
	 */
	public Float getFixedBonus() {
		return fixedBonus;
	}

	/**
	 * @param fixedBonus the fixedBonus to set
	 */
	public void setFixedBonus(Float fixedBonus) {
		this.fixedBonus = fixedBonus;
	}

	/**
	 * @return the medicalInsurance
	 */
	public Float getMedicalInsurance() {
		return medicalInsurance;
	}

	/**
	 * @param medicalInsurance the medicalInsurance to set
	 */
	public void setMedicalInsurance(Float medicalInsurance) {
		this.medicalInsurance = medicalInsurance;
	}

	/**
	 * @return the employerPf
	 */
	public Float getEmployerPf() {
		return employerPf;
	}

	/**
	 * @param employerPf the employerPf to set
	 */
	public void setEmployerPf(Float employerPf) {
		this.employerPf = employerPf;
	}

	/**
	 * @return the employeePf
	 */
	public Float getEmployeePf() {
		return employeePf;
	}

	/**
	 * @param employeePf the employeePf to set
	 */
	public void setEmployeePf(Float employeePf) {
		this.employeePf = employeePf;
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
	 * @return the vehicleRunning
	 */
	public Float getVehicleRunning() {
		return vehicleRunning;
	}

	/**
	 * @param vehicleRunning the vehicleRunning to set
	 */
	public void setVehicleRunning(Float vehicleRunning) {
		this.vehicleRunning = vehicleRunning;
	}

	/**
	 * @return the telReimbursement
	 */
	public Float getTelReimbursement() {
		return telReimbursement;
	}

	/**
	 * @param telReimbursement the telReimbursement to set
	 */
	public void setTelReimbursement(Float telReimbursement) {
		this.telReimbursement = telReimbursement;
	}

	/**
	 * @return the employerEsi
	 */
	public Float getEmployerEsi() {
		return employerEsi;
	}

	/**
	 * @param employerEsi the employerEsi to set
	 */
	public void setEmployerEsi(Float employerEsi) {
		this.employerEsi = employerEsi;
	}

	/**
	 * @return the employeeEsi
	 */
	public Float getEmployeeEsi() {
		return employeeEsi;
	}

	/**
	 * @param employeeEsi the employeeEsi to set
	 */
	public void setEmployeeEsi(Float employeeEsi) {
		this.employeeEsi = employeeEsi;
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
	 * @return the isPfInPercent
	 */
	public Boolean getIsPfInPercent() {
		return isPfInPercent;
	}

	/**
	 * @param isPfInPercent the isPfInPercent to set
	 */
	public void setIsPfInPercent(Boolean isPfInPercent) {
		this.isPfInPercent = isPfInPercent;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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
