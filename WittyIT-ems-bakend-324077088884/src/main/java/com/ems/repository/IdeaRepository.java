package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.Idea;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
	
	/**
	 * Find by id
	 * @param id
	 * @return Designation
	 */
	Idea findById(Long id);
	
	/**
	 * Find by Employee id
	 * @param id the Employee Id
	 * @return Designation
	 */
	List<Idea> findByEmpId(Long empId);
	
	/**
	 * Find by Manager id
	 * @param id the Employee Id
	 * @return Designation
	 */
	List<Idea> findByManagerIdAndManagerShared(Long empId , Boolean isManagerIncluded);

}
