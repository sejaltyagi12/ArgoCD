package com.ems.wrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.ems.domain.Department;
import com.ems.domain.Designation;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DepartmentWithDesignationListWrapper {
	private long departmentId;
	private String departmentName;
	private List<DesignationWrapper> designationWrappers;

	public DepartmentWithDesignationListWrapper() {
	}

	public DepartmentWithDesignationListWrapper(Department department, List<Designation> designations) {
		this.departmentId = department.getDeptId();
		this.departmentName = department.getDeptName();
		this.designationWrappers = new ArrayList<DesignationWrapper>();
		for (Designation designation : designations) {
			this.designationWrappers.add(new DesignationWrapper(designation));
		}
	}

	/**
	 * @return the departmentId
	 */
	public long getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId
	 *            the departmentId to set
	 */
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName
	 *            the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the designationWrappers
	 */
	public List<DesignationWrapper> getDesignationWrappers() {
		return designationWrappers;
	}

	/**
	 * @param designationWrappers
	 *            the designationWrappers to set
	 */
	public void setDesignationWrappers(List<DesignationWrapper> designationWrappers) {
		this.designationWrappers = designationWrappers;
	}

}
