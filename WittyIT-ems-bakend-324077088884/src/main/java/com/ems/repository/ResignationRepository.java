package com.ems.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.domain.Employee;
import com.ems.domain.LeaveHistory;
import com.ems.domain.Resignation;

public interface ResignationRepository extends JpaRepository<Resignation, Long> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return Resignation
	 */
	Resignation findById(Long id);

	/**
	 * Find by ManagerId
	 * 
	 * @param id the ManagerId
	 * @return Resignation List
	 */
	List<Resignation> findByManagerId(Long managerId);

	/**
	 * Find last resignation of the employee
	 * 
	 * @param employee
	 * @return
	 */
	@Query("select r from Resignation r where r.employee=:employee ORDER BY r.creationDate DESC")
	Resignation latestResignation(@Param("employee") Employee employee);

	/**
	 * Find by Employee
	 * 
	 * @param Employee the Employee
	 * @return Resignation List
	 */
	List<Resignation> findByEmployee(Employee employee);

	/**
	 * Find by Employee and manager status and Hr status
	 * 
	 * @param Employee the Employee
	 * @return Resignation List
	 */
	List<Resignation> findByEmployeeAndManagerStatusAndHrStatus(Employee employee, Resignation.Status managerStatus, Resignation.Status hrStatus);

	/**
	 * Find by Employee and manager status and Hr status
	 * 
	 * @param Employee the Employee
	 * @return Resignation List
	 */
	@Query("select r from Resignation r where r.employee = :employee and (managerStatus = :managerStatus or hrStatus = :hrStatus)")
	List<Resignation> findByEmployeeAndManagerStatusOrHrStatus(@Param("employee")Employee employee, @Param("managerStatus")Resignation.Status managerStatus, @Param("hrStatus")Resignation.Status hrStatus);

	/**
	 * Find by Employee and Hr status
	 * 
	 * @param Employee the Employee
	 * @return Resignation List
	 */
	List<Resignation> findByEmployeeAndHrStatus(Employee employee, Resignation.Status hrStatus);

	@Query(value = "select * from resignation r join employee e on r.emp_id = e.id join designation d on e.designation_id = d.designation_id where convert(substring_index(d.level,'_',-1),unsigned integer) < :level and not e.id = :empId", nativeQuery = true)
	List<Resignation> findAllButHimselfWithLessLevel(@Param("level") Integer level,@Param("empId") Long empId);

	/**
	 * find employee resignation date
	 * @param id
	 * @return
	 */
	@Query("select r.resignationDate from Resignation r where r.employee.id=:id and r.hrStatus='APPROVED' order by r.resignationDate desc")
	Date findLastResignationDateByEmployeeId(@Param("id") Long id);

}
