package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.EvaluationGroup;


public interface EvaluationGroupRepository extends JpaRepository<EvaluationGroup, Long> {
	
	
	/**
	 * Find by id
	 * @param id
	 * @return Evaluation Group
	 */
	EvaluationGroup findById(Long id);

	/**
	 * Find all Evaluation Groups
	 * @return EvaluationGroup List.
	 */
	List<EvaluationGroup> findAll();
	
	/**
	 * Find all Evaluation Active Groups
	 * @return EvaluationGroup List.
	 */
	List<EvaluationGroup> findByIsActive(Boolean isActive);
	
	/**
	 * Find all Evaluation by order Id and is active
	 * @return EvaluationGroup List.
	 */
	List<EvaluationGroup> findByOrderIndexAndIsActive(Integer orderIndex, Boolean isActive);

}
