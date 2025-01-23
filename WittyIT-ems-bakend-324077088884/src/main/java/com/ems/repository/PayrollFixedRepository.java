package com.ems.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.domain.Employee;
import com.ems.domain.PayrollFixed;

public interface PayrollFixedRepository extends JpaRepository<PayrollFixed, Long>{
	
	/**
	 * Find by id
	 * 
	 * @param id
	 * @return Payroll Fixed
	 */
	PayrollFixed findById(Long id);
	
	/**
	 * Find by Employee and Active
	 * 
	 * @param employee
	 * @param active
	 * 
	 * @return Payroll Fixed List
	 */
	List<PayrollFixed> findByEmployeeAndActive(Employee employee, Boolean active);
	
	/**
	 * Find by Employee and Deleted
	 * 
	 * @param employee
	 * @param deleted
	 * 
	 * @return Payroll Fixed List
	 */
	List<PayrollFixed> findByEmployeeAndDeleted(Employee employee, Boolean deleted);
	
	/**
	 * Find by Employee valid from and Deleted
	 * 
	 * @param employee
	 * @param deleted
	 * 
	 * @return Payroll Fixed List
	 */
	//List<PayrollFixed> findByEmployeeAndValidFromAndDeleted(Employee employee, DateTime validFrom, Boolean deleted);
	
	/**
	 * Find by Employee valid from and Deleted
	 * 
	 * @param employee
	 * @param deleted
	 * 
	 * @return Payroll Fixed List
	 */
	@Query(value="select p from PayrollFixed p where p.employee = :employee and p.deleted = :deleted and :date between p.validFrom and p.expiry")
	List<PayrollFixed> findByEmployeeAndValidFromAndDeleted( @Param("employee")Employee employee, @Param("date") DateTime paySlipDate, @Param("deleted")Boolean deleted);

}
