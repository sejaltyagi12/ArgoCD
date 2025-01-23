package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.ResignationReason;

/**
 * @author Avinash Tyagi
 *
 */
public interface ResignationReasonRepository extends JpaRepository<ResignationReason, Long> {
	
	
	
	/**
	 * Find by id
	 * @param id
	 * @return ResignationReason
	 */
	ResignationReason findById(Long id);


}
