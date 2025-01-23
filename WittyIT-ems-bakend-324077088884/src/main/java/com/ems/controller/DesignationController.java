package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Designation;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.DesignationService;
import com.ems.wrappers.DepartmentWithDesignationListWrapper;
import com.ems.wrappers.DesignationWrapper;

@RestController
@RequestMapping("/designation")
public class DesignationController {

	@Autowired
	private DesignationService designationService;

	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * Get list of All Designation Service url: /designation/list method: GET
	 *
	 * @return List of all Designation
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	List<DesignationWrapper> getDesignations() throws ServiceException {
		return designationService.getDesignations();
	}

	/**
	 * Get specific designation by ID
	 * Service url:  /designation method: GET 
	 * 
	 * @param designationId
	 * @return Designation
	 * @throws ServiceException the service exception
	 */
	@GetMapping(value = "/{designationId}")
	Designation getDesignationById(@PathVariable("designationId") Long designationId) throws ServiceException {
		return designationService.getDesignationById(designationId);
	}

	/**
	 * Add a designation
	 * Service url:  /designation method: POST
	 * 
	 * @param designationWrapper
	 * @return Designation
	 * @throws ServiceException the service exception
	 */
	@PostMapping
	Designation addDesignation(@RequestBody DesignationWrapper designationWrapper) throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access to these.");
		return designationService.saveDesignation(designationWrapper);
	}

	/**
	 * Edit a designation
	 * Service url:  /designation method: PUT
	 * 
	 * @param designationId
	 * @param designationWrapper
	 * @return Designation
	 * @throws ServiceException the service exception
	 */
	@PutMapping(value = "/{designationId}")
	Designation updateDesignation(@PathVariable("designationId") Long designationId, @RequestBody DesignationWrapper designationWrapper) throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access to these.");
		return designationService.upadteDesignation(designationId, designationWrapper);
	}

	/**
	 * Delete a designation 
	 * Service url:  /designation method: DELETE
	 * 
	 * @param designationId
	 * @throws ServiceException the service exception
	 */
	@DeleteMapping(value = "/{designationId}")
	void deleteDesignation(@PathVariable("designationId") Long designationId) throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access to these.");
		designationService.deleteDesignation(designationId);
	}

	/**
	 * Get list of all Department with designation in it.
	 * 
	 * URL: /designation/department
	 * 
	 * @return List of Department with Designation in it.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/department", method = RequestMethod.GET)
	List<DepartmentWithDesignationListWrapper> getDesignationsWithDepartment() throws ServiceException {
		return designationService.getDesignationsWithDepartment();
	}

	public DesignationController() {
		// TODO Auto-generated constructor stub
	}

}