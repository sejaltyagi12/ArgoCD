package com.ems.wrappers;

import org.joda.time.DateTime;

import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class EventWrapper {

	private Long id;

	private String firstName;

	private String middleName;

	private String lastName;

	private String empCode;

	private String departmentName;

	private String avatarURL;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime eventDate;

	private String eventType;
	
	private String anniversaryYear;

	public String getAnniversaryYear() {
		return anniversaryYear;
	}

	public void setAnniversaryYear(String anniversaryYear) {
		this.anniversaryYear = anniversaryYear;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	/**
	 * @return the eventDate
	 */
	public DateTime getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate
	 *            the eventDate to set
	 */
	public void setEventDate(DateTime eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * Gets The Employee Last name.
	 *
	 * @return the Employee Last name
	 */
	public String getFullName() {

		String fullName = this.firstName;

		fullName = fullName.trim() + (this.middleName != null ? (" " + this.middleName) : "");

		fullName = fullName.trim() + (this.lastName != null ? (" " + this.lastName) : "");

		return fullName;
	}

	public EventWrapper(Long id, String firstName, String middleName, String lastName, String empCode,
			String departmentName, String avatarURL, DateTime eventDate, String eventType, String anniversaryYear) {
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.empCode = empCode;
		this.departmentName = departmentName;
		this.avatarURL = avatarURL;
		this.eventDate = eventDate;
		this.eventType = eventType;
		this.anniversaryYear = anniversaryYear;
	}

	public EventWrapper() {
	}

}
