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

import com.ems.domain.Department;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private AuthenticationService authenticationService;
	

	/**
	 * Get list of All department Service url: /department/list method: GET
	 *
	 * @return List of all department
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	List<Department> getDepartments() throws ServiceException {
		return departmentService.getDepartments();
	}
	
	/**
	 * Get specific department by ID
	 * Service url:  /department method: GET 
	 * 
	 * @param deptId
	 * @return Department
	 * @throws ServiceException the service exception
	 */
	@GetMapping(value = "/{deptId}")
	Department getDepartmentById(@PathVariable("deptId") Integer deptId) throws ServiceException {
		return departmentService.getDepartmentById(deptId);
	}

	/**
	 * Add a department
	 * Service url:  /department method: POST
	 * 
	 * @param department
	 * @return Department
	 * @throws ServiceException the service exception
	 */
	@PostMapping
	Department addDepartment(@RequestBody Department department) throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access to these.");
		return departmentService.saveDepartment(department);
	}

	/**
	 * Edit a department
	 * Service url:  /department method: PUT
	 * 
	 * @param deptId
	 * @param department
	 * @return Department
	 * @throws ServiceException the service exception
	 */
	@PutMapping(value = "/{deptId}")
	Department updateDepartment(@PathVariable("deptId") Integer deptId, @RequestBody Department department)
			throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access to these.");
		return departmentService.upadteDepartment(deptId, department);
	}

	/**
	 * Delete a department 
	 * Service url:  /department method: DELETE
	 * 
	 * @param deptId
	 * @throws ServiceException the service exception
	 */
	@DeleteMapping(value = "/{deptId}")
	void deleteDepartment(@PathVariable("deptId") Integer deptId) throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access to these.");
		departmentService.deleteDepartment(deptId);
	}

	public DepartmentController() {
		// TODO Auto-generated constructor stub
	}

}