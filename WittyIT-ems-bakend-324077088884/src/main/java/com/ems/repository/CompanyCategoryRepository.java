package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.CompanyCategory;


public interface CompanyCategoryRepository extends JpaRepository<CompanyCategory, Integer> {
	
	
	/**
	 * Find by id
	 * @param id
	 * @return CompanyCategory
	 */
	CompanyCategory findByCompanyId(Integer id);

	/**
	 * Find all CompanyCategory
	 * @return CompanyCategory
	 */
	List<CompanyCategory> findAll();

}
