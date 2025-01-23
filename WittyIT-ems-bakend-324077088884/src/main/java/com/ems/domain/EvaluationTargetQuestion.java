package com.ems.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class EvaluationTargetQuestion {
	
	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="rating_id",nullable =false)
	private EvaluationRating rating;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="group_id",nullable =false)
	private EvaluationGroup evaluationGroup;
	
	@OneToMany(mappedBy = "targetQuestion", cascade = CascadeType.ALL)
    List<EvaluationTarget> evaluationTargets;
	
	@Transient
	private Integer orderIndex;

	@CreatedDate
	@JsonIgnore
	private DateTime creationDate;
	
	@LastModifiedDate
	@JsonIgnore
	private DateTime modifiedDate;
	
	@CreatedBy
	@JsonIgnore
	private Long createdBy;
	
	@LastModifiedBy
	@JsonIgnore
	private Long modifiedBy;
	
	@NotNull
	private Boolean isActive = true;
	

	public Integer getOrderIndex() {
		if(!evaluationTargets.isEmpty())
			setOrderIndex(evaluationTargets.get(0).getOrderIndex());
		
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public List<EvaluationTarget> getEvaluationTargets() {
		return evaluationTargets;
	}

	public void setEvaluationTargets(List<EvaluationTarget> evaluationTargets) {
		this.evaluationTargets = evaluationTargets;
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

	public EvaluationRating getRating() {
		return rating;
	}

	public void setRating(EvaluationRating rating) {
		this.rating = rating;
	}

	public EvaluationGroup getEvaluationGroup() {
		return evaluationGroup;
	}

	public void setEvaluationGroup(EvaluationGroup evaluationGroup) {
		this.evaluationGroup = evaluationGroup;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public EvaluationTargetQuestion() {
	}

	public EvaluationTargetQuestion(String name, String description,
			EvaluationRating rating, EvaluationGroup evaluationGroup) {
		this.name = name;
		this.description = description;
		this.rating = rating;
		this.evaluationGroup = evaluationGroup;
	}

}
