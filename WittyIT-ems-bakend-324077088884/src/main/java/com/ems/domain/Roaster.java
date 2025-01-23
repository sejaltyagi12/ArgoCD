package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Table(name = "roaster_history")
@EntityListeners(AuditingEntityListener.class)
public class Roaster {
	
	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="roaster_type_id",nullable =false)
	private RoasterType roasterType;

	@NotNull
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime roasterDate;
	
	@NotNull
	private Long empId;
	
	@JsonIgnore
	@CreatedDate
	private DateTime creationDate;
	
	@JsonIgnore
	@LastModifiedDate
	private DateTime modifiedDate;
	
	@JsonIgnore
	@CreatedBy
	private Long createdBy;
	
	@JsonIgnore
	@LastModifiedBy
	private Long modifiedBy;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoasterType getRoasterType() {
		return roasterType;
	}

	public void setRoasterType(RoasterType roasterType) {
		this.roasterType = roasterType;
	}

	public DateTime getRoasterDate() {
		return roasterDate;
	}

	public void setRoasterDate(DateTime roasterDate) {
		this.roasterDate = roasterDate;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
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

	public Roaster(RoasterType roasterType, DateTime roasterDate, Long empId) 
	{
		this.roasterType = roasterType;
		this.roasterDate = roasterDate;
		this.empId = empId;
	}

	public Roaster() {
	}
	
	
	

}
