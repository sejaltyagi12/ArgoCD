package com.ems.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ems.domain.Employee;
import com.ems.exception.ServiceException;
import com.ems.security.EmsUserDetails;
import com.ems.servicefinder.utils.Constants;

@Service
public class AuthenticationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Check user admin and authentication
	 * 
	 * @param user
	 *            current user
	 * @return boolean
	 * @throws ServiceException
	 */
	/*
	 * public boolean isAdmin() throws ServiceException { Employee employee =
	 * employeeService .findById(getEmployeeByAuthentication().getId()); if
	 * (employee == null) { throw new ServiceException("The user is null"); }
	 * 
	 * if (!employee.isActive()) { throw new
	 * ServiceException("The user is not active"); }
	 * 
	 * if (employee.hasRole(RoleName.ROLE_ADMIN)) return true;
	 * 
	 * return false; }
	 */

	/**
	 * Check user authentication
	 * 
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean isAuthenticated() throws ServiceException {
		try {
			return SecurityContextHolder.getContext().getAuthentication() == null ? false
					: SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
		} catch (Exception e) {
			logger.error("Authentication error", e);
			throw new ServiceException("Authentication error", e);
		}
	}

	private EmsUserDetails getEmployeeByAuthentication() throws ServiceException {
		try {
			return (EmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (NullPointerException e) {
			// e.printStackTrace();
			logger.error("Authentication is null", e);
			throw new ServiceException("Authentication is null", e);
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Authentication error", e);
			throw new ServiceException("Authentication error", e);
		}
	}

	public Employee getAuthenticatedEmployee() throws ServiceException {
		Employee employee = employeeService.findById(getEmployeeByAuthentication().getId());

		if (employee == null) {
			throw new ServiceException("The user is null");
		}

		if (employee.isActive() != null && !employee.isActive()) {
			throw new ServiceException("The user is not active");
		}

		return employee;
	}

	/**
	 * Check if user is SuperAdmin and authentication
	 * 
	 * @param user
	 *            current user
	 * @return boolean
	 * @throws ServiceException
	 */
	/*
	 * public boolean isSuperAdmin() throws ServiceException { User user =
	 * userService .findById(getEmployeeByAuthentication().getId()); if (user ==
	 * null) { throw new ServiceException("The user is null"); }
	 * 
	 * if (!user.isActive()) { throw new
	 * ServiceException("The user is not active"); }
	 * 
	 * if (user.getFirm().isActive() == false) { throw new
	 * ServiceException("Firm is not active"); }
	 * 
	 * if (user.hasRole(RoleName.ROLE_SUPER_ADMIN)) return true;
	 * 
	 * return false; }
	 * 
	 * 
	 * private boolean isSuperAdmin(User user) { for (Role role :
	 * user.getRoles()) { if (role.getName().compareTo("ROLE_SUPER_ADMIN") == 0)
	 * return true; } return false; }
	 */

	public boolean isEmployee() throws ServiceException {
		Employee employee = getAuthenticatedEmployee();

		if (employee.getRole().getRoleName().equalsIgnoreCase("EMPLOYEE"))
			return true;

		return false;
	}

	public boolean isAdmin() throws ServiceException {
		Employee employee = getAuthenticatedEmployee();

		if (employee.getRole().getRoleName().equalsIgnoreCase("ADMIN"))
			return true;

		return false;
	}

	public boolean isHR() throws ServiceException {
		Employee employee = getAuthenticatedEmployee();
		return isHR(employee);
	}

	public boolean isHrOrAdmin() throws ServiceException {
		Employee employee = getAuthenticatedEmployee();
		return isHR(employee) || isAdmin(employee);
	}

	public boolean isSoftwareDeveloper() throws ServiceException {
		Employee employee = getAuthenticatedEmployee();
		String department = employee.getDesignation().getDepartment().getDeptName();
		return department.equalsIgnoreCase("Software Development");
	}

	public boolean isAdmin(Employee employee) throws ServiceException {

		if (employee.getRole().getRoleName().equalsIgnoreCase("ADMIN"))
			return true;

		return false;
	}

	public boolean isHR(Employee employee) throws ServiceException {
		String department = employee.getDesignation().getDepartment().getDeptName();
		if (department.equalsIgnoreCase("Human Resource") || department.equalsIgnoreCase("hr")
				|| department.equalsIgnoreCase("hr dept")) {
			int level = Integer.parseInt(employee.getDesignation().getLevel().split("_")[1]);
			if (level >= Constants.HR_ACCESS_LEVEL)
				return true;
		}
		return false;
	}
}