package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class EvaluationTarget {
	
	@Id
	@GeneratedValue
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="target_question_id",nullable =false)
	private EvaluationTargetQuestion targetQuestion;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="design_id",nullable =false)
	private Designation designation;
	
	@NotNull
	private Integer orderIndex;
	
	@JsonIgnore
	@LastModifiedDate
	private DateTime closureDate;

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
	@JsonIgnore
	private Boolean isActive = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EvaluationTargetQuestion getTargetQuestion() {
		return targetQuestion;
	}

	public void setTargetQuestion(EvaluationTargetQuestion targetQuestion) {
		this.targetQuestion = targetQuestion;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public DateTime getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(DateTime closureDate) {
		this.closureDate = closureDate;
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

	public EvaluationTarget() {
	}

	public EvaluationTarget(EvaluationTargetQuestion targetQuestion,
			Designation designation, Integer orderIndex) {
		this.targetQuestion = targetQuestion;
		this.designation = designation;
		this.orderIndex = orderIndex;
	}

	
}
