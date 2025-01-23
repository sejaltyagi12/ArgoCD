package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Avatar;
import com.ems.domain.Employee;


/**
 * AvatarRepository is a JpaRepository needed to maintain Avatar objects.
 *
 * @author Avinash Tyagi
 */
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

	/**
	 * Find avatar by employee
	 * @param employee the employee
	 * @return the avatar
	 */
	Avatar findByEmployee(Employee employee);
	
	/**
	 * Delete avatar by employee
	 * @param profile the employee
	 */
	@Modifying
	@Transactional
	void deleteByEmployee(Employee employee);
}
