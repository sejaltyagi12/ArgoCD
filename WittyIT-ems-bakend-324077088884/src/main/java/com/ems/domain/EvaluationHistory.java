package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
public class EvaluationHistory {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="employee_id",nullable =false)
	@JsonIgnore
	private Employee employee;
	
	@NotNull
	private Long managerId = (long) 0;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="target_id",nullable =false)
	private EvaluationTarget target;
	
	private Integer employeeRating;
	
	private String employeeReason;
	
	@NotNull
	private Boolean employeeSubmitted = false;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime employeeSubmittedDate;
	
	private Integer managerRating;
	
	private String managerReason;
	
	@NotNull
	private Boolean managerSubmitted = false;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime managerSubmittedDate;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cycle_id",nullable =false)
	private EvaluationCycle cycle;
	
	private Long finalReviewBy;
	
	@CreatedDate
	@JsonIgnore
	private DateTime creationDate;
	
	@LastModifiedDate
	@JsonIgnore
	private DateTime modifiedDate;
	
	@CreatedBy
	@JsonIgnore
	private Long createdBy;
	
	@LastModifiedBy
	@JsonIgnore
	private Long modifiedBy;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public EvaluationTarget getTarget() {
		return target;
	}

	public void setTarget(EvaluationTarget target) {
		this.target = target;
	}

	public Integer getEmployeeRating() {
		return employeeRating;
	}

	public void setEmployeeRating(Integer employeeRating) {
		this.employeeRating = employeeRating;
	}

	public String getEmployeeReason() {
		return employeeReason;
	}

	public void setEmployeeReason(String employeeReason) {
		this.employeeReason = employeeReason;
	}

	public Boolean getEmployeeSubmitted() {
		return employeeSubmitted;
	}

	public void setEmployeeSubmitted(Boolean employeeSubmitted) {
		this.employeeSubmitted = employeeSubmitted;
	}

	public DateTime getEmployeeSubmittedDate() {
		return employeeSubmittedDate;
	}

	public void setEmployeeSubmittedDate(DateTime employeeSubmittedDate) {
		this.employeeSubmittedDate = employeeSubmittedDate;
	}

	public Integer getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(Integer managerRating) {
		this.managerRating = managerRating;
	}

	public String getManagerReason() {
		return managerReason;
	}

	public void setManagerReason(String managerReason) {
		this.managerReason = managerReason;
	}

	public Boolean getManagerSubmitted() {
		return managerSubmitted;
	}

	public void setManagerSubmitted(Boolean managerSubmitted) {
		this.managerSubmitted = managerSubmitted;
	}

	public DateTime getManagerSubmittedDate() {
		return managerSubmittedDate;
	}

	public void setManagerSubmittedDate(DateTime managerSubmittedDate) {
		this.managerSubmittedDate = managerSubmittedDate;
	}

	public EvaluationCycle getCycle() {
		return cycle;
	}

	public void setCycle(EvaluationCycle cycle) {
		this.cycle = cycle;
	}

	public Long getFinalReviewBy() {
		return finalReviewBy;
	}

	public void setFinalReviewBy(Long finalReviewBy) {
		this.finalReviewBy = finalReviewBy;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public EvaluationHistory() {
	}

	public EvaluationHistory(Employee employee, EvaluationTarget target,
			EvaluationCycle cycle, Long managerId) {
		this.employee = employee;
		this.target = target;
		this.cycle = cycle;
		this.managerId = managerId;
	}
	
}
