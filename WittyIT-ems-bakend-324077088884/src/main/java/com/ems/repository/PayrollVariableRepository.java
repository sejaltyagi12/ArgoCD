package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.Employee;
import com.ems.domain.PayrollVariable;

public interface PayrollVariableRepository extends JpaRepository<PayrollVariable, Long>{
	
	/**
	 * Find by id
	 * 
	 * @param id
	 * @return Payroll Variable
	 */
	PayrollVariable findById(Long id);
	

	/**
	 * Find by Employee 
	 * 
	 * @param employee
	 * 
	 * @return Payroll variable List
	 */
	List<PayrollVariable> findByEmployee(Employee employee);
	
	/**
	 * Find by Employee , Month and Year 
	 * 
	 * @param employee
	 * 
	 * @return Payroll variable List
	 */
	PayrollVariable findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

}
