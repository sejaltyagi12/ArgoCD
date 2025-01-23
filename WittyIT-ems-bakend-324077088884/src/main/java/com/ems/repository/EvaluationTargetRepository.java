package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.domain.Designation;
import com.ems.domain.EvaluationGroup;
import com.ems.domain.EvaluationTarget;
import com.ems.domain.EvaluationTargetQuestion;

public interface EvaluationTargetRepository extends
		JpaRepository<EvaluationTarget, Long> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return Evaluation Target
	 */
	EvaluationTarget findById(Long id);
	
	/**
	 * Find by findBy Designation, TargetQuestion, OrderIndex, IsActive
	 * 
	 * @param Designation
	 * @param TargetQuestion
	 * @param OrderIndex
	 * @param IsActive
	 * 
	 * @return Evaluation Target
	 */
	EvaluationTarget findByDesignationAndTargetQuestionAndOrderIndexAndIsActive(Designation designation, EvaluationTargetQuestion targetQuestion, Integer order , Boolean isActive);

	
	/**
	 * Find by findBy TargetQuestion and IsActive
	 * 
	 * @param TargetQuestion
	 * @param IsActive
	 * 
	 * @return Evaluation Target List
	 */
	List<EvaluationTarget> findByTargetQuestionAndIsActive(EvaluationTargetQuestion targetQuestion, Boolean isActive);
	
	
	/**
	 * Find by findBy Designation and IsActive
	 * 
	 * @param Designation
	 * @param IsActive
	 * 
	 * @return Evaluation Target List
	 */
	List<EvaluationTarget> findByDesignationAndIsActive(Designation designation, Boolean isActive);
	
	/**
	 * Find by Designation, Group and IsActive
	 * 
	 * @param Designation
	 * @param IsActive
	 * @param Group
	 * 
	 * @return Evaluation Target List
	 */
	@Query(value = "select t from EvaluationTarget t where t.designation = :designation and t.targetQuestion.evaluationGroup = :group and t.isActive = :isActive")
	List<EvaluationTarget> findByDesignationAndGroupAndIsActive(@Param("designation")Designation designation, @Param("group")EvaluationGroup group, @Param("isActive")Boolean isActive);
	
	/**
	 * Find all Evaluation Target
	 * 
	 * @return EvaluationTarget List.
	 */
	List<EvaluationTarget> findAll();

}
