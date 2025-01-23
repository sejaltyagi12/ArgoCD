package com.ems.wrappers;


import com.ems.domain.EvaluationRating;

public class EvaluationQuestionWrapper implements Comparable<EvaluationQuestionWrapper> {
	
	
	private Long id;

	private String name;
	
	private String description;
	
	private EvaluationRating rating;
	
    private EvaluationTargetWrapper target;
	
	private Integer orderIndex;

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

	public EvaluationTargetWrapper getTarget() {
		return target;
	}

	public void setTarget(EvaluationTargetWrapper target) {
		this.target = target;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public EvaluationQuestionWrapper() {
	}

	public EvaluationQuestionWrapper(Long id, String name, String description,
			EvaluationRating rating, EvaluationTargetWrapper target,
			Integer orderIndex) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.rating = rating;
		this.target = target;
		this.orderIndex = orderIndex;
	}
	
	@Override
    public int compareTo(EvaluationQuestionWrapper wrapper) {
       return (int) (this.getId() - wrapper.getId());
    }

}
