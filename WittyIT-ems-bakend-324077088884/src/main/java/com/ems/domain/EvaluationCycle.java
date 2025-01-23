package com.ems.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
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
public class EvaluationCycle {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime startDate;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime endDate;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime employeeEndDate;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime managerEndDate;
	
	@NotNull
	private Boolean isCompleted = false;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="department_id", nullable=false)
	private Department department;
	
	@NotNull
	@Transient
	private String userStatus = "New";
	
	@NotNull
	@Transient
	private List<Integer> departments = new ArrayList<>();
	
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
	

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public DateTime getEmployeeEndDate() {
		return employeeEndDate;
	}

	public void setEmployeeEndDate(DateTime employeeEndDate) {
		this.employeeEndDate = employeeEndDate;
	}

	public DateTime getManagerEndDate() {
		return managerEndDate;
	}

	public void setManagerEndDate(DateTime managerEndDate) {
		this.managerEndDate = managerEndDate;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Integer> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Integer> departments) {
		this.departments = departments;
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
	

	public EvaluationCycle(DateTime startDate, DateTime endDate,
			DateTime employeeEndDate, DateTime managerEndDate,
			Department department) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.employeeEndDate = employeeEndDate;
		this.managerEndDate = managerEndDate;
		this.department = department;
	}

	public EvaluationCycle() {
	}

}
