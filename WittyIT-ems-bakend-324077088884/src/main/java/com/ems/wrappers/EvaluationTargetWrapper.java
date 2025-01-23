package com.ems.wrappers;

import org.joda.time.DateTime;

import com.ems.domain.EvaluationHistory;

public class EvaluationTargetWrapper {

	private Long id;

	private EvaluationHistory evaluationHistory;

	private DateTime closureDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EvaluationHistory getEvaluationHistory() {
		return evaluationHistory;
	}

	public void setEvaluationHistory(EvaluationHistory evaluationHistory) {
		this.evaluationHistory = evaluationHistory;
	}

	public DateTime getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(DateTime closureDate) {
		this.closureDate = closureDate;
	}

	public EvaluationTargetWrapper(Long id,
			EvaluationHistory evaluationHistory, DateTime closureDate) {
		super();
		this.id = id;
		this.evaluationHistory = evaluationHistory;
		this.closureDate = closureDate;
	}
	
	public EvaluationTargetWrapper() {
		
	}
	
	

}
