package com.ems.wrappers;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class HolidayWrapper {
	
	private Integer id;

	@NotNull
	private String name;

	@NotNull
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime holidayDate;
	
	private String companyName;
	
	private String imageUrl;
	
	@NotNull
	private Integer companyId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(DateTime holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public HolidayWrapper(Integer id, String name, DateTime holidayDate,
			String companyName, Integer companyId, String imageUrl) {
		this.id = id;
		this.name = name;
		this.holidayDate = holidayDate;
		this.companyName = companyName;
		this.companyId = companyId;
		this.imageUrl = imageUrl;
	}

	public HolidayWrapper() {
	}
	
	
	

}
