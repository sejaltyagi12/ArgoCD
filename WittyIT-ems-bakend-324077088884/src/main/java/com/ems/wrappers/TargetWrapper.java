package com.ems.wrappers;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.ems.domain.EvaluationGroup;
import com.ems.domain.EvaluationRating;

public class TargetWrapper {
	
	private Long id;
	
	@NotNull
	private String name = "";
	
	@NotNull
	private String description = "";
	
	@NotNull
	private Long groupId;
	
	@NotNull
	private Long ratingId;
	
	private EvaluationGroup group;
	
	private EvaluationRating rating;
	
	@NotNull
	private Integer order = 0;
	
	@NotNull
	private List<Long> designation;

	public EvaluationGroup getGroup() {
		return group;
	}

	public void setGroup(EvaluationGroup group) {
		this.group = group;
	}

	public EvaluationRating getRating() {
		return rating;
	}

	public void setRating(EvaluationRating rating) {
		this.rating = rating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getRatingId() {
		return ratingId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}

	public List<Long> getDesignation() {
		return designation;
	}

	public void setDesignation(List<Long> designation) {
		this.designation = designation;
	}

	public TargetWrapper() {
	}

	public TargetWrapper(String name, String description,
			Long groupId, Long ratingId, Integer order, List<Long> designation) {
		super();
		this.name = name;
		this.description = description;
		this.groupId = groupId;
		this.ratingId = ratingId;
		this.order = order;
		this.designation = designation;
	}

}
