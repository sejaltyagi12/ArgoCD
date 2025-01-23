package com.ems.wrappers;

import java.util.List;


public class TargetListWrapper implements Comparable<TargetListWrapper>{
	
	private Long categoryId;
	
	private String category;
	
	private Integer categoryOrderIndex;
	
	private List<EvaluationQuestionWrapper> questions;
	

	public Integer getCategoryOrderIndex() {
		return categoryOrderIndex;
	}

	public void setCategoryOrderIndex(Integer categoryOrderIndex) {
		this.categoryOrderIndex = categoryOrderIndex;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<EvaluationQuestionWrapper> getQuestions() {
		return questions;
	}

	public void setQuestions(List<EvaluationQuestionWrapper> questions) {
		this.questions = questions;
	}
	
	public TargetListWrapper() {
	}

	public TargetListWrapper(Long categoryId, String category,
			List<EvaluationQuestionWrapper> questions, Integer orderIndex) {
		this.categoryId = categoryId;
		this.category = category;
		this.questions = questions;
		this.categoryOrderIndex = orderIndex;
	}
	
	@Override
    public int compareTo(TargetListWrapper wrapper) {
       return (int) (this.getCategoryOrderIndex() - wrapper.getCategoryOrderIndex());
    }
	
}
