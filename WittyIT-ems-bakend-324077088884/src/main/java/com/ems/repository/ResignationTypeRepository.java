package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.ResignationType;

/**
 * @author Avinash Tyagi
 *
 */
public interface ResignationTypeRepository extends
		JpaRepository<ResignationType, Long> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return ResignationReason
	 */
	ResignationType findById(Long id);

}
