package com.ems.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ems.security.EmsUserDetails;
import com.ems.service.EmployeeService;

public class SpringSecurityAuditorAware implements AuditorAware<Long> {

	@Autowired
	private EmployeeService employeeService;

	public Long getCurrentAuditor() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

//		Setting Super admin's id for forgot password.

//		if (authentication.getPrincipal().equals("anonymousUser")) {
//			return employeeService.getSuperAdmin();
//		}
		
		if(authentication.getPrincipal() instanceof EmsUserDetails) {
			return ((EmsUserDetails) authentication.getPrincipal()).getId();
		}
		return employeeService.getSuperAdmin();
	}
}