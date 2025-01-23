package com.ems.wrappers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ApplyLeaveWrapper {

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime fromDate;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime toDate;

	private String fromDateAsString;

	private String toDateAsString;

	private Float dayCount;

	private String employeeComments;

	private Integer typeId;

	private MultipartFile fileContent;

	public ApplyLeaveWrapper() {
	}

	public ApplyLeaveWrapper(DateTime fromDate, DateTime toDate, Float dayCount, Integer typeId) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.dayCount = dayCount;
		this.typeId = typeId;
	}

	/**
	 * @return the fromDateAsString
	 */
	public String getFromDateAsString() {
		return fromDateAsString;
	}

	/**
	 * @param fromDateAsString
	 *            the fromDateAsString to set
	 */
	public void setFromDateAsString(String fromDateAsString) {
		if (fromDate == null)
		fromDate = DateTime.parse(fromDateAsString, DateTimeFormat.forPattern("MM/dd/yyyy"));
		this.fromDateAsString = fromDateAsString;
	}

	/**
	 * @return the toDateAsString
	 */
	public String getToDateAsString() {
		return toDateAsString;
	}

	/**
	 * @param toDateAsString
	 *            the toDateAsString to set
	 */
	public void setToDateAsString(String toDateAsString) {
		if (toDate == null)
		toDate = DateTime.parse(toDateAsString, DateTimeFormat.forPattern("MM/dd/yyyy"));
		this.toDateAsString = toDateAsString;
	}

	public DateTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(DateTime fromDate) {
		this.fromDate = fromDate;
	}

	public DateTime getToDate() {
		return toDate;
	}

	public void setToDate(DateTime toDate) {
		this.toDate = toDate;
	}

	public Float getDayCount() {
		return dayCount;
	}

	public void setDayCount(Float dayCount) {
		this.dayCount = dayCount;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public MultipartFile getFileContent() {
		return fileContent;
	}

	public void setFileContent(MultipartFile fileContent) {
		this.fileContent = fileContent;
	}
}
