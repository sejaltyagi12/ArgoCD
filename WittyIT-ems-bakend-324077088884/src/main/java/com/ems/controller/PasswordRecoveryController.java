package com.ems.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ems.domain.Employee;
import com.ems.exception.ServiceException;
import com.ems.service.EmployeeService;
import com.ems.wrappers.EmployeeRegistrationWrapper;

@Controller
@RequestMapping("/password/recovery")
class PasswordRecoveryController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Send a password recover message. The user will receive via email
	 * instructions to reset the password 
	 * Service url: /password/recovery/email method: POST
	 * 
	 * @param Employee
	 *            Employee to send the instructions
	 * @param request
	 *            HttpServletRequest to get the url and generate the rest
	 *            password link
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/email", method = RequestMethod.POST)
	@ResponseBody
	void sendPasswordRecovery(@RequestBody Employee employee,
			HttpServletRequest request) throws ServiceException {
		employeeService.sendPasswordRecovery(employee, request);
	}

	/**
	 * Reset user's password based on a previous request. The token will be
	 * verified on this service 
	 * Service url: /password/recovery/reset method: POST
	 *
	 * @param request
	 *            the request
	 * @throws ServiceException 
	 * @see com.ems.wrappers.EmployeeRegistrationWrapper
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	@ResponseBody
	void update(@RequestBody EmployeeRegistrationWrapper wrapper, HttpServletRequest request) throws ServiceException {
		 employeeService.updatePassword(wrapper , request);
	}
	
}