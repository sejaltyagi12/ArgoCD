package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.EvaluationGroup;
import com.ems.domain.EvaluationTargetQuestion;

public interface EvaluationTargetQuestionRepository extends
		JpaRepository<EvaluationTargetQuestion, Long> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return Evaluation Question
	 */
	EvaluationTargetQuestion findById(Long id);

	/**
	 * Find all Evaluation Rating
	 * 
	 * @return EvaluationQuestion List.
	 */
	List<EvaluationTargetQuestion> findAll();
	
	/**
	 * Find all by Evaluation Group and active
	 * 
	 * @return EvaluationQuestion List.
	 */
	List<EvaluationTargetQuestion> findByEvaluationGroupAndIsActive(EvaluationGroup group, Boolean isActive);
	
	/**
	 * Find all by Name and active
	 * 
	 * @return EvaluationQuestion.
	 */
	EvaluationTargetQuestion findByNameAndEvaluationGroupAndIsActive(String name, EvaluationGroup group, Boolean isActive);

}
