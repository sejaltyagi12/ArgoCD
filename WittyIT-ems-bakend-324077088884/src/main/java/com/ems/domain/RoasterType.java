package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class RoasterType {
	
	@Id
	@GeneratedValue
	private Integer id;

	@NotNull
	private String type;

	@NotNull
	private Integer roasterTypeId;
	
	@NotNull
	private String description;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRoasterTypeId() {
		return roasterTypeId;
	}

	public void setRoasterTypeId(Integer roasterTypeId) {
		this.roasterTypeId = roasterTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
