package com.ems.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.Period;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.stereotype.Component;

import com.ems.domain.Employee;
import com.ems.security.EmsUserDetails;

/**
 * Handles user's authentication.
 * 
 */
@Component
public class EmsUserDetailsService implements UserDetailsService {

	//private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	protected EmployeeService employeeService;

	@Autowired
	protected MessageSource messageSource;

	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	private static final Logger log= Logger.getLogger(EmsUserDetails.class.getName());
	
	/**
	 * Checks user's credentials (email and password), if he belongs to the firm, 
	 *
	 * @param username the username
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("inside loadUserByUsername with email : "+email);
		Employee employee = employeeService.findByEmail(email);

		if (employee == null) {
			log.info("employee is null");
			throw new UsernameNotFoundException("The email or password you entered is incorrect.");
		} else if (!employee.isActive()) {
			log.info("employee is not active");
			throw new UsernameNotFoundException("User is not active.");}
		else if (employee.isDeleted()) {
			log.info("employee is Deleted");
			String message = getMessage("user.not-active");
			throw new UsernameNotFoundException(message);
		}
		// check login attemps of user
		if (employee.getLoginAttempts() > 3) {
			log.info("login attempts is more than 3 ");
			// check if user is try to login after 6 hours then again he/she will able to
			// login 4 times
			if (employee.getAccountUnlockTime() != null && employee.getAccountUnlockTime().isBefore(DateTime.now())) {
				log.info("employee is logged in again after 6 hours so they have again 3 attempts");
				employee.setLoginAttempts(0);
				employee.setAccountUnlockTime(null);
				employeeService.saveOnly(employee);
			} else {
				log.info("throwing exception inside more then 3 login attempts");
				Period remainingTime = new Period(DateTime.now(), employee.getAccountUnlockTime());
				throw new LockedException(String.format(
						"Account has been blocked due to multiple unsuccessful login attempts. Please try again after %d hours, %d minutes, and %d seconds.",
						remainingTime.getHours(), remainingTime.getMinutes(), remainingTime.getSeconds()));
			}
		}
		// if password will not match then it will increase user login attemps and set
		// account unlock time.
		if (!passwordEncoder.matches(request.getParameter("password"), employee.getPassword())) {
			log.info("enter password doesn't match increasing login attempts ");
			employee.setLoginAttempts(employee.getLoginAttempts() + 1);
			employee.setLastFailedLoginAttempt(DateTime.now());
			// after 3 wrong password attempts we will set account unlock time so user will
			// try to enter right password after 6 hours.
			if (employee.getLoginAttempts() == 4) {
				employee.setAccountUnlockTime(DateTime.now().plusHours(6));
			}
			log.info("login attempts " + employee.getLoginAttempts());
			// saveing employee
			employeeService.saveOnly(employee);
		}
		// if user enter right password after 1,2 or 3 login attempts then he will login
		// and its login attempts is set to 0.
		else if (employee.getLoginAttempts() > 0
				&& passwordEncoder.matches(request.getParameter("password"), employee.getPassword())) {
			log.info("login attempts set to be zero again");
			employee.setLoginAttempts(0);
			employeeService.saveOnly(employee);
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
		
		UserDetails userDetails = new EmsUserDetails(employee.getId(), email, employee.getPassword(), authorities);

		// UserDetails userDetails = new EmsUserDetails(1, "abc",
		// "$2a$10$teJrCEnsxNT49ZpXU7n22O27aCGbVYYe/RG6/XxdWPJbOLZubLIi2" ,
		// authorities);
	    
		return userDetails;

	}

	private String getMessage(String messageKey) {

		String m = messageSource.getMessage(messageKey, null, null);

		return m;

	}

}