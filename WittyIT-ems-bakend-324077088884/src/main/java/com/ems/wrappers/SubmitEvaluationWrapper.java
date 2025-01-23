package com.ems.wrappers;


import org.joda.time.DateTime;

import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SubmitEvaluationWrapper {
	
	private Long id;
	
	private Long employeeId;
	
	private Long targetId;
	
	private Integer employeeRating;
	
	private String employeeReason;
	
	private Boolean employeeSubmitted = false;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime employeeSubmittedDate;
	
	private Integer managerRating;
	
	private String managerReason;
	
	private Boolean managerSubmitted = false;
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime managerSubmittedDate;

	private Long cycleId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

}
