package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "designation")
public class Designation {

	@Id
	@GeneratedValue
	private Long designationId;

	@NotNull
	private String designation;

	@NotNull
	private String level;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dept_id", nullable = false)
	private Department department;

	public Designation() {
		super();

	}

	public Designation(Long designationId, String designationName, String level) {
		this.designationId = designationId;
		this.designation = designationName;
		this.level = level;
	}

	/**
	 * Sets the Designation Name.
	 *
	 * @param id the designationName
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * Gets The Designation Name.
	 *
	 * @return the designationName
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * Sets the Designation Id.
	 *
	 * @param id the Designation id
	 */
	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	/**
	 * Gets The Designation Id.
	 *
	 * @return the Designation Id
	 */
	public Long getDesignationId() {
		return designationId;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

}
