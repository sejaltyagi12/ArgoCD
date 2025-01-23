package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ems.domain.Department;


public interface DepartmentRepository extends JpaRepository<Department, Long> {
	
	
	/**
	 * Find by id
	 * @param id
	 * @return Department
	 */
	Department findByDeptId(Integer deptId);
	
	/**
	 * Find all Department
	 * @return Department list of all departments
	 */
	List<Department> findAll();
	
	/**
	 * Find all department in ordered
	 * @return Department list
	 */
	@Query("select d from Department d order by d.deptId")
	List<Department> findAllOrderByDeptId();
		
	
	/**
	 * Find by department name
	 * @param deptName
	 * @return Department
	 */
	Department findByDeptName(String deptName);

}
