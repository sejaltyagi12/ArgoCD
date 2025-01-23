package com.ems.wrappers;

public class UpdateProfileWrapper {
	private String fieldName;
	private String previousValue;
	private String newValue;

	public UpdateProfileWrapper(String fieldName, String previousValue, String newValue) {
		this.fieldName = fieldName;
		this.previousValue = previousValue;
		this.newValue = newValue;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the previousValue
	 */
	public String getPreviousValue() {
		return previousValue;
	}

	/**
	 * @param previousValue
	 *            the previousValue to set
	 */
	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	/**
	 * @return the newValue
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue
	 *            the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

}
