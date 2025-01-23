package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.Designation;


public interface DesignationRepository extends JpaRepository<Designation, Long> {
	
	
	/**
	 * Find by id
	 * @param id
	 * @return Designation
	 */
	Designation findByDesignationId(Long designationId);
	
	/**
	 * Find all Designation
	 * @return Designation List of all designations
	 */
	List<Designation> findAll();
	
	Designation findByDesignation(String designation);

}
