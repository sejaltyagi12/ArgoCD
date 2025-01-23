package com.ems.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.ems.domain.Employee;
import com.ems.service.EmployeeService;

/**
 * Used for verifying credentials of an user with every request. If an user
 * details is changes while logged in, this will cause an exception.
 * 
 * @author Wittybrains
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class CustomFilter extends GenericFilterBean {

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	@Lazy
	private EmployeeService employeeService;

	@Autowired
	@Qualifier("defaultTokenServices")
	private ConsumerTokenServices tokenServices;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String token = extractHeaderToken(req);

		// Verifying if token is present and valid by tokenStore
		if (token != null && !token.isEmpty() && tokenStore.readAuthentication(token) != null) {
			OAuth2Authentication auth = tokenStore.readAuthentication(token);

			Employee employee = employeeService.findByEmail(auth.getName());
			
			if (employee == null) {
				tokenServices.revokeToken(token);
			} else if (employee.isDeleted()) {
				tokenServices.revokeToken(token);
			} else if (!employee.isActive()) {
				tokenServices.revokeToken(token);
			}
		}
		chain.doFilter(request, res);

	}

	// Used for extracting the Bearer token from the HttpRequest
	protected String extractHeaderToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders("Authorization");
		while (headers.hasMoreElements()) { // typically there is only one (most
											// servers enforce that)
			String value = headers.nextElement();
			if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
				String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
				// Add this here for the auth details later. Would be better to
				// change the signature of this method.
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
						value.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
				int commaIndex = authHeaderValue.indexOf(',');
				if (commaIndex > 0) {
					authHeaderValue = authHeaderValue.substring(0, commaIndex);
				}
				return authHeaderValue;
			}
		}

		return null;
	}

}
