package com.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.CompanyCategory;
import com.ems.repository.CompanyCategoryRepository;

@Service
public class CompanyCategoryService {

	@Autowired
	private CompanyCategoryRepository companyCategoryRepository;

	/**
	 * Find by id.
	 *
	 * @param id
	 *            
	 * @return the CompanyCategory
	 */
	public CompanyCategory findById(Integer id) {
		return companyCategoryRepository.findByCompanyId(id);
	}
	
	/**
	 * Fetch all CompanyCategory.
	 *
	 *            
	 * @return the CompanyCategory
	 */
	public List<CompanyCategory> getCompanyCategories() {
		return companyCategoryRepository.findAll();

	}

}
