package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Role;
import com.ems.exception.ServiceException;
import com.ems.service.RoleService;


@RestController
@RequestMapping("/role")
public class RoleDetailsController {
	
	@Autowired
	private RoleService roleService;
	
	
	/**
	 * Get list of Roles 
	 * Service url:  /role/details method: GET 
	 *
	 * @return List of all Roles
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	List<Role> getRoles() throws ServiceException 
	{
		return roleService.getRoles();
	}
	
	
	public RoleDetailsController() {
		// TODO Auto-generated constructor stub
	}

}