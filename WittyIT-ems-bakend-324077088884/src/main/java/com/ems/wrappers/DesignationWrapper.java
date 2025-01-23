package com.ems.wrappers;

import javax.validation.constraints.NotNull;

import com.ems.domain.Designation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DesignationWrapper {
	private Long designationId;
	
	@JsonProperty("designation")
	private String designationName;
	
	private String level;
	
	@NotNull
	private Integer deptId;
	private String deptName;

	public DesignationWrapper() {
	}

	public DesignationWrapper(Designation designation) {
		this.designationId = designation.getDesignationId();
		this.designationName = designation.getDesignation();
	}
	
	public DesignationWrapper(Long designationId, String designationName,
			String level, Integer deptId, String deptName) {
		this.designationId = designationId;
		this.designationName = designationName;
		this.level = level;
		this.deptId = deptId;
		this.deptName = deptName;
	}

	/**
	 * @return the designationId
	 */
	public Long getDesignationId() {
		return designationId;
	}

	/**
	 * @param designationId
	 *            the designationId to set
	 */
	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	/**
	 * @return the designationName
	 */
	public String getDesignationName() {
		return designationName;
	}

	/**
	 * @param designationName
	 *            the designationName to set
	 */
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	

}
