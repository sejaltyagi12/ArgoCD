package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "department")
public class Department {

	@Id
	@GeneratedValue
	private Integer deptId;

	@NotNull
	private String deptName;

	/**
	 * Sets the Department Name.
	 *
	 * @param id
	 *            the deptName
	 * 
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * Gets The Department Name.
	 *
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * Sets the Department Id.
	 *
	 * @param id
	 *            the Department id
	 * 
	 */
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	/**
	 * Gets The Department Id.
	 *
	 * @return the Department Id
	 */
	public Integer getDeptId() {
		return deptId;
	}

}
