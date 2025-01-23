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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Holiday {
	
	@Id
	@GeneratedValue
	private Integer id;

	@NotNull
	private String name;

	@NotNull
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime holidayDate;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="comp_id",nullable =false)
	private CompanyCategory companyCategory;
	
	private String imageUrl;
	
	@CreatedDate
	private DateTime creationDate;
	
	@LastModifiedDate
	private DateTime modifiedDate;
	
	@CreatedBy
	private Long createdBy;
	
	@LastModifiedBy
	private Long modifiedBy;
	
	
	/**
	 * Gets  The holiday id.
	 *
	 * @return the holiday id
	 */
	public Integer getId() {
		return id;
	}

	
	/**
	 * Sets the holiday id.
	 *
	 * @param id the holiday id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	

	/**
	 * Gets  The holiday name.
	 *
	 * @return the holiday name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the holiday name.
	 *
	 * @param name the holiday name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Sets the holiday Date.
	 *
	 * @param holidayDate the holiday Date
	 */
	public void setHolidayDate(DateTime holidayDate) {
		this.holidayDate = holidayDate;
	}

	
	/**
	 * Gets  The holiday Date.
	 *
	 * @return the holiday Date
	 */
	public DateTime getHolidayDate() {
		return holidayDate;
	}

	
	/**
	 * Gets  The holiday related company.
	 *
	 * @return the company
	 */
	public CompanyCategory getCompanyCategory() {
		return companyCategory;
	}
	

	/**
	 * Sets the holiday related company.
	 *
	 * @param company the holiday related company
	 */
	public void setCompanyCategory(CompanyCategory companyCategory) {
		this.companyCategory = companyCategory;
	}

	
	/**
	 * Gets  The holiday creation Date.
	 *
	 * @return the holiday creationDate
	 */
	public DateTime getCreationDate() {
		return creationDate;
	}

	
	/**
	 * Sets the holiday creation Date.
	 *
	 * @param Date the holiday creationDate
	 */
	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	
	/**
	 * Gets  The holiday modified Date.
	 *
	 * @return the holiday modifiedDate
	 */
	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	
	/**
	 * Sets the holiday modified Date.
	 *
	 * @param Date the holiday modifiedDate
	 */
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

	public Holiday(Integer id, String name, DateTime holidayDate) {
		this.id = id;
		this.name = name;
		this.holidayDate = holidayDate;
	}

	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public Holiday() {
	}
	
	
}
