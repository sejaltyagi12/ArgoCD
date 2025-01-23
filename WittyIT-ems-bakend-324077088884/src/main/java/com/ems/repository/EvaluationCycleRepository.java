package com.ems.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.Department;
import com.ems.domain.EvaluationCycle;

public interface EvaluationCycleRepository extends JpaRepository<EvaluationCycle, Long>{
	
	/**
	 * Find by id
	 * @param id
	 * @return EvaluationCycle
	 */
	EvaluationCycle findById(Long id);
	
	/**
	 * Find by Is Completed
	 * @param isCompleted
	 * @return EvaluationCycle
	 */
	List<EvaluationCycle> findByIsCompleted(Boolean isCompleted);
	
	/**
	 * Find by department
	 * @param isCompleted
	 * @return EvaluationCycle
	 */
	List<EvaluationCycle> findByDepartment(Department department);
	
	/**
	 * Find by Is Completed
	 * @param isCompleted
	 * @return EvaluationCycle
	 */
	EvaluationCycle findByDepartmentAndIsCompleted(Department department, Boolean isCompleted);
	
	/**
	 * Find by Manager end date Is Completed
	 * @param isCompleted
	 * @param managerEndDate 
	 * @return EvaluationCycle
	 */
	EvaluationCycle findByManagerEndDateAfterAndIsCompleted(DateTime managerEndDate, Boolean isCompleted);

}
