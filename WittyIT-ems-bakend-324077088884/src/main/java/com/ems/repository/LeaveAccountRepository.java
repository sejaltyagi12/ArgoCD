package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.LeaveAccount;

public interface LeaveAccountRepository extends
		JpaRepository<LeaveAccount, Long> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return LeaveAccount
	 */
	LeaveAccount findById(Long id);
	
	/**
	 * Find by employeeID and Year
	 * 
	 * @param id
	 * @param year
	 * @return LeaveAccount
	 */
	LeaveAccount findByEmpIdAndYear(Long empId, Integer year);
	
	public void deleteByEmpId(Long empId);

}
