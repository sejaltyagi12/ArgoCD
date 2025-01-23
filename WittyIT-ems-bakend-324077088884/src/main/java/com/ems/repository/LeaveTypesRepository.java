package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.domain.LeaveType;

public interface LeaveTypesRepository extends JpaRepository<LeaveType, Integer> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return LeaveType
	 */
	LeaveType findByTypeId(Integer id);

	/**
	 * Find all LeaveTypes
	 * 
	 * @return LeaveTypes
	 */
	List<LeaveType> findAll();

	@Query("SELECT leave FROM LeaveType leave where leave.type IN :leaveType")
	List<LeaveType> findAllLeave(@Param("leaveType") List<String> leaveType);

}
