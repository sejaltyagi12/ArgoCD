package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.domain.Employee;
import com.ems.domain.EvaluationCycle;
import com.ems.domain.EvaluationHistory;
import com.ems.domain.EvaluationTarget;

public interface EvaluationHistoryRepository extends JpaRepository<EvaluationHistory, Long>{
	
	/**
	 * Find by id
	 * @param id
	 * @return EvaluationHistory
	 */
	EvaluationHistory findById(Long id);
	
	
	/**
	 * Find by manager id, Cycle and employee Submitted. 
	 * @param id
	 * @return EvaluationHistory
	 */
	List<EvaluationHistory> findByManagerIdAndCycleAndEmployeeSubmitted(Long managerId, EvaluationCycle cycle, Boolean isEmployeeSubmitted);
	
	/**
	 * Find Employee count by manager id and cycleCompleted and employeeSubmitted 
	 * 
	 * @param id the manager id
	 * @param employeesubmitted flag
	 * @param cyclecompleted flag
	 * @return count
	 */
	@Query("select count(distinct e.employee) from EvaluationHistory e where e.cycle.isCompleted=:cycleCompleted and e.employeeSubmitted=:employeeSubmitted and e.managerId=:manager")
	int findEmployeeCountByManagerIdAndEmployeeSubmittedAndCycleCompleted(@Param("manager")Long managerId, @Param("employeeSubmitted")Boolean isEmployeeSubmitted, @Param("cycleCompleted")Boolean cycleCompleted);
	
	/**
	 * Find Employee count by cycleCompleted and employeeSubmitted 
	 * 
	 * @param employeesubmitted flag
	 * @param cyclecompleted flag
	 * @return count
	 */
	@Query("select count(distinct e.employee) from EvaluationHistory e where e.cycle.isCompleted=:cycleCompleted and e.employeeSubmitted=:employeeSubmitted")
	int findEmployeeCountByEmployeeSubmittedAndCycleCompleted(@Param("employeeSubmitted")Boolean isEmployeeSubmitted, @Param("cycleCompleted")Boolean cycleCompleted);
	
	
	/**
	 * Find by employee, target, Cycle and employee Submitted. 
	 * @param id
	 * @return EvaluationHistory
	 */
	EvaluationHistory findByEmployeeAndTargetAndCycleAndEmployeeSubmitted(Employee employee,EvaluationTarget target, EvaluationCycle cycle, Boolean isEmployeeSubmitted);
	
	/**
	 * Find by employee, target, Cycle and employee Submitted. 
	 * @param id
	 * @return EvaluationHistory
	 */
	EvaluationHistory findByEmployeeAndTargetAndCycle(Employee employee,EvaluationTarget target, EvaluationCycle cycle);
	
	
	/**
	 * Find by Employee and Cycle. 
	 * @param employee
	 * @param cycle
	 * 
	 * @return EvaluationHistory List
	 */
	List<EvaluationHistory> findByEmployeeAndCycle(Employee employee, EvaluationCycle cycle);
	
	/**
	 * Find by Employee and Cycle. 
	 * @param employee
	 * @param cycle
	 * 
	 * @return EvaluationHistory List
	 */
	@Query(value = "select t from EvaluationHistory t where t.employee = :employee and t.cycle.isCompleted = true group by t.cycle")
	List<EvaluationHistory> findByEmployeeAndGroupByCycle(@Param("employee") Employee employee);
	
	/**
	 * Find by Employee and Cycle. 
	 * @param employee
	 * @param cycle
	 * 
	 * @return EvaluationHistory List
	 */
	@Query(value = "select t from EvaluationHistory t where t.employee = :employee and t.cycle = :cycle group by t.cycle")
	EvaluationHistory findByEmployeeAndCycleGroupByCycle(@Param("employee") Employee employee, @Param("cycle") EvaluationCycle cycle);
	
	/**
	 * Find by cycles Manager id. 
	 * @param manager Id
	 * 
	 * @return EvaluationHistory List
	 */
	@Query(value = "select t from EvaluationHistory t where t.managerId = :managerId group by t.cycle")
	List<EvaluationHistory> findByManagerIdAndGroupByCycle(@Param("managerId") Long managerId);
	
	/**
	 * Find by Cycle.
	 * @param id
	 * @return EvaluationHistory
	 */
	List<EvaluationHistory> findByCycle(EvaluationCycle cycle);
	
	/**
	 * Find by Cycle and employee Submitted. 
	 * @param id
	 * @return EvaluationHistory
	 */
	List<EvaluationHistory> findByCycleAndEmployeeSubmitted(EvaluationCycle cycle, Boolean isEmployeeSubmitted);
	
	/**
	 * Find by Designation, Group and IsActive
	 * 
	 * @param Designation
	 * @param IsActive
	 * @param Group
	 * 
	 * @return Evaluation Target List
	 */
	@Query(value = "select t from EvaluationHistory t where t.cycle = :cycle group by t.target.targetQuestion")
	List<EvaluationHistory> findCycleByGroupByTargets(@Param("cycle")EvaluationCycle cycle);

}
