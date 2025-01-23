package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Avatar;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.EmployeeService;
import com.ems.wrappers.ChangePasswordWrapper;
import com.ems.wrappers.EmployeeChartWrapper;
import com.ems.wrappers.EmployeeRegistrationWrapper;
import com.ems.wrappers.EmployeeWrapper;
import com.ems.wrappers.EventWrapper;
import com.ems.wrappers.FileUploadWrapper;

@RestController
@RequestMapping("/employee")
public class EmployeeDetailsController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * get details of employee or user Service url: /employee/details method:
	 * GET
	 *
	 * @return EmployeeWrapper with employee data in it
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.EmployeeWrapper
	 */

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	EmployeeWrapper getEmployeeDetails() throws ServiceException {
		return employeeService.getEmployeeDetails();
	}

	/**
	 * Change the password of an employee Service url: /employee/changePassword
	 * method: POST
	 *
	 * @param Change
	 *            Password Wrapper
	 * @return EmployeeWrapper with employee data in it
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.EmployeeWrapper
	 * @see com.ems.wrappers.ChangePasswordWrapper
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	EmployeeWrapper changePassword(@RequestBody ChangePasswordWrapper passwordWrapper) throws ServiceException {
		return new EmployeeWrapper(
				employeeService.changePassword(passwordWrapper.getOldPassword(), passwordWrapper.getNewPassword()));
	}

	/**
	 * Resets the password of an employee Service url: /employee/resetPassword
	 * method: POST
	 *
	 * @param Change
	 *            Password Wrapper
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.ChangePasswordWrapper
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	void resetPassword(@RequestBody ChangePasswordWrapper passwordWrapper) throws ServiceException {
		employeeService.resetPassword(passwordWrapper);
	}

	/**
	 * Get list of all employees (only for Admin). Service url: /employee/getAll
	 * method: GET
	 *
	 * @return List of all Employees in employee wrapper response
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.EmployeeWrapper
	 */
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	List<EmployeeWrapper> getAllEmployees() throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("You are not Admin");

		return employeeService.getAllEmployees();
	}

	/**
	 * Get details of an employee (only for Admin). Service url: /employee/get
	 * method: GET
	 *
	 * @param id
	 *            Employee id
	 * @return EmployeeWrapper with details of employee
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.EmployeeWrapper
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	EmployeeWrapper getEmployee(@RequestParam("id") Long id) throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access.");

		return employeeService.createEmployeeWrapper(employeeService.findByEmployeeId(id));
	}

	/**
	 * Add a new Employee (only for Admin). Service url: /employee/registration
	 * method: POST
	 *
	 * @param EmployeeRegistrationWrapper
	 *            to be saved
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.EmployeeRegistrationWrapper
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	void saveEmployeeDetails(@RequestBody EmployeeRegistrationWrapper employee) throws ServiceException {
		employeeService.saveEmployee(employee);
	}

	/**
	 * Edit an Employee (only for Admin). Service url: /employee/edit method:
	 * PUT
	 *
	 * @param EmployeeRegistrationWrapper
	 *            to be saved
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.EmployeeRegistrationWrapper
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	void editEmployeeDetails(@RequestBody EmployeeRegistrationWrapper employee) throws ServiceException {
		if (authenticationService.isAdmin() || authenticationService.isHR())
			employeeService.editEmployee(employee);
	}

	/**
	 * Activate or deactivates an Employee (only for Admin). Service url:
	 * /employee/activate method: PUT
	 *
	 * @param id
	 *            Employee id
	 * @param active
	 *            flag
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.PUT)
	void editEmployeeDetails(@RequestParam(value = "id") Long id, @RequestParam(value = "active") Boolean active)
			throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("You don't have rights.");

		employeeService.editEmployeeStatus(id, active);
	}

	/**
	 * Resign and deactivates an Employee (only for Admin). Service url:
	 * /employee/resign method: PUT
	 *
	 * @param id
	 *            Employee id @param active flag @throws ServiceException the
	 *            service exception @throws
	 */
	@RequestMapping(value = "/resign", method = RequestMethod.PUT)
	void resignEmployee(@RequestBody EmployeeRegistrationWrapper wrapper) throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("You don't have rights.");

		employeeService.resignEmployee(wrapper);
	}

	/**
	 * Generates the employee related charts data (only for Admin).
	 * 
	 * Service url: /employee/edit method: GET
	 *
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.EmployeeChartWrapper
	 */
	@RequestMapping(value = "/getChartData", method = RequestMethod.GET)
	EmployeeChartWrapper getChartData() throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access");

		return employeeService.getEmployeeChartData();
	}

	/**
	 * Upload user avatar Service url: /employee/avatar/upload method: POST.
	 * method: POST.
	 *
	 * @param avatar
	 *            Avatar bean containing the file to be uploaded
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/avatar/upload", method = RequestMethod.POST)
	String changeAvatar(Avatar avatar) throws ServiceException {
		return employeeService.changeAvatar(avatar);
	}

	/**
	 * Gets all upcoming employees birthdays Service url:
	 * /employee/upcoming/birthdays method: GET.
	 *
	 * @return Birthdays of all employees in next 30 days
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/upcoming/events", method = RequestMethod.GET)
	List<EventWrapper> getUpcommingEvents() throws ServiceException {
		return employeeService.getUpcommingEvents();
	}

	/**
	 * Set employee read policy Service url: /employee/policy/accept method:
	 * PUT.
	 *
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/policy/accept", method = RequestMethod.PUT)
	void setEmployeePolicy() throws ServiceException {
		employeeService.setEmployeePolicy(authenticationService.getAuthenticatedEmployee(), true);
	}

	/** Will be used to store multiple employee data at once.
	 * @param fileUploadWrapper
	 * @return
	 * @throws ServiceException
	 */
	@PostMapping("/registerall")
	String registerAll(FileUploadWrapper fileUploadWrapper) throws ServiceException {
		return employeeService.registerAll(fileUploadWrapper);
	}
	
	/**
	 * Activate or Deactivate employee
	 * @param id
	 * @param status
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/active/inactive/{userId}/{isActive}", method = RequestMethod.PUT)
	void activateOrDeactivateEmployee(@PathVariable(value="userId") Long userId, @PathVariable(value="isActive") boolean status)
			throws ServiceException {
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("You don't have rights.");

		employeeService.activateOrDeactivateEmployee(userId, status);
	}

}
