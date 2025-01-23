package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.CompanyCategory;
import com.ems.exception.ServiceException;
import com.ems.service.CompanyCategoryService;


@RestController
@RequestMapping("/company")
public class CompanyCategoryController {
	
	@Autowired
	private CompanyCategoryService companyCategoryService;
	
	/**
	 * Get list of Company categories 
	 * Service url:  /company/list method: GET 
	 *
	 * @return List of all categories
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	List<CompanyCategory> getCompanyCategories() throws ServiceException 
	{
		return companyCategoryService.getCompanyCategories();
	}
	
	
	public CompanyCategoryController() {
		// TODO Auto-generated constructor stub
	}

}