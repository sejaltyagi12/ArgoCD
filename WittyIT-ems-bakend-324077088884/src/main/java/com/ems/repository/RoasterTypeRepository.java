package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.RoasterType;

public interface RoasterTypeRepository extends JpaRepository<RoasterType, Long> {

	/**
	 * Find by id
	 * @param id
	 * @return RoasterType
	 */
	RoasterType findById(Long id);
	
	/**
	 * Find by Roaster Type
	 * @param id
	 * @return Role
	 */
	RoasterType findByType(String type);
	
}
