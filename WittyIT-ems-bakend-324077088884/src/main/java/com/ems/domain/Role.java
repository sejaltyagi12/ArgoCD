package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue
	private Integer roleId;

	@NotNull
	private String roleName;

	@NotNull
	private Integer id;

	/**
	 * Sets the Id.
	 *
	 * @param id
	 *            the id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets The Role Id.
	 *
	 * @return the Role Id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the Role Name.
	 *
	 * @param roleName the roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Gets The Role Name.
	 *
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Sets the Role Id.
	 *
	 * @param roleId the Role id
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets The Role Id.
	 *
	 * @return the Role Id
	 */
	public Integer getRoleId() {
		return roleId;
	}

}