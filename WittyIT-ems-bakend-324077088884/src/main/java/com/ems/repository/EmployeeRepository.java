package com.ems.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Employee;
import com.ems.wrappers.ChartData;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	/**
	 * Find by email and not deleted
	 * 
	 * @param email
	 *            the email
	 * @return Employee
	 */
	Employee findByOfficialEmailAndDeleted(String email, boolean deleted);

	/**
	 * Find by emp Code
	 * 
	 * @param code
	 *            the email
	 * @return Employee
	 */
	Employee findByEmpCode(String empCode);

	/**
	 * Find by Active
	 * 
	 * @param acive
	 *            flag
	 * @return Employee list
	 */
	List<Employee> findByIsActive(Boolean isActive);

	/**
	 * Find by id and not deleted
	 * 
	 * @param email
	 *            the email
	 * @return Employee
	 */
	Employee findByIdAndDeleted(Long id, boolean deleted);
	
	Employee findByOfficialEmail(String officialEmail);

	/**
	 * Find by id and not deleted
	 * 
	 * @param email
	 *            the email
	 * @return Employee
	 */
	Employee findByEmpCodeAndDeleted(String empCode, boolean deleted);

	/**
	 * Find by Joining dates
	 * 
	 * @param startDate
	 * @param enddate
	 * @return Employee List
	 */
	List<Employee> findByJoiningDateBetween(Date startDate, Date endDate);
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Query("select e from Employee e where e.role.roleName = :roleName order by id")
	List<Employee> findByRole(@Param("roleName") String roleName);
	
	@Query("select e from Employee e where e.managerId=:managerId")
	List<Employee> findAllByManagerId(@Param("managerId") Long managerId);

	@Query("select e from Employee e join e.designation.department d where d.deptName=:deptName and e.deleted=:deleted")
	List<Employee> findAllHRAndNotDeleted(@Param("deptName") String deptName, @Param("deleted") Boolean deleted);

	/**
	 * Employee count in each company for chart except 1,2 as they are default employees.
	 * 
	 * @return list of chart data
	 * @see com.ems.wrappers.ChartData
	 */
	@Query(value = "select new com.ems.wrappers.ChartData(  "
			+ "e.companyCategory.companyName as name, count(e.companyCategory.companyId) as count) from Employee e "
			+ " where e.isActive = :isActive and e.id not in (1,2) group by e.companyCategory.companyName")
	List<ChartData> getCompanyEmployeesGraphData(@Param("isActive") Boolean isActive);

	/**
	 * Employee count in each department for chart except 1,2 as they are default employees.
	 * 
	 * @return list of chart data
	 * @see com.ems.wrappers.ChartData
	 */
	@Query(value = "select new com.ems.wrappers.ChartData(  "
			+ "e.designation.department.deptName as name, count(e.designation.department.deptId) as count) from Employee e "
			+ " where e.isActive = :isActive and e.id not in (1,2) group by e.designation.department.deptName")
	List<ChartData> getDepartmentEmployeesGraphData(@Param("isActive") Boolean isActive);

	/**
	 * Employee count in each designation for chart except 1,2 as they are default employees.
	 * 
	 * @return list of chart data
	 * @see com.ems.wrappers.ChartData
	 */
	@Query(value = "select new com.ems.wrappers.ChartData(  "
			+ "e.designation.designation as name, count(e.designation.designation) as count) from Employee e "
			+ " where e.isActive = :isActive and e.id not in (1,2) group by e.designation.designation")
	List<ChartData> getDesignationEmployeesGraphData(@Param("isActive") Boolean isActive);

	/**
	 * Find all birthdays between dates
	 * 
	 * @return list of Employees
	 */
	@Query(value = "select e from Employee e where e.isActive = :isActive and DATE_FORMAT(e.dob, '%m/%d') BETWEEN :startDate AND :endDate")
	List<Employee> getUpcomingBirthdays(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("isActive") Boolean isActive);

	@Query(value = "select e from Employee e where e.isActive = :isActive and DATE_FORMAT(e.joiningDate, '%m/%d') BETWEEN :startDate AND :endDate")
	List<Employee> getUpcomingAnniversary(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("isActive") Boolean isActive);
	


	/**
	 * Find by Active and Ids not in.
	 * 
	 * @param acive
	 *            flag
	 * @return Employee list
	 */
	List<Employee> findByIsActiveAndIdNotIn(Boolean isActive, List<Long> ids);
	
	/**
	 * Find employee is in resignation table
	 * @param id
	 * @return BigInteger
	 */
	@Query(nativeQuery =  true, value = "SELECT COUNT(r.id) > 0 FROM resignation r WHERE r.emp_id = :id")
	BigInteger isEmployeeInResignationTable(@Param("id") Long id);
	/**
	 * Find employee in employee table with already exist bank account number
	 * @param bankAccountNumber
	 * @return
	 */
	@Query("select count(e.id)>0 from Employee e where e.bankAccountNumber=:bankAccountNumber")
	boolean isBankAccountNumberAlreadyExistOrNot(@Param("bankAccountNumber") String bankAccountNumber);

	
}
