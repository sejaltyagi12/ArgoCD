package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "leave_type")
public class LeaveType {
	
	@Id
	private Integer typeId;
	
	private String type;

	
	/**
	 * Gets The Leave Type Id.
	 *
	 * @return the Leave Type Id.
	 */
	public Integer getTypeId() {
		return typeId;
	}

	/**
	 * Sets the Leave Type Id.
	 *
	 * @param typeId the Leave Type id
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	/**
	 * Gets The Leave Type.
	 *
	 * @return the Leave Type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the Leave Type.
	 *
	 * @param type the Leave Type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	

}
