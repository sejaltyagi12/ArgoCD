package com.ems.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	// private static String REALM="MY_OAUTH_REALM";

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	TokenEnhancer tokenEnhancer;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory().withClient("witty-portal").secret("1234567890")
				.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ADMIN", "EMPLOYEE", "CLIENT", "USER",
						"ROLE_ANONYMOUS")
				.scopes("read", "write", "trust")
				.authorizedGrantTypes("password", "authorization_code", "implicit", "client_credentials");

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore);
		defaultTokenServices.setTokenEnhancer(tokenEnhancer);
		defaultTokenServices.setSupportRefreshToken(false);
		endpoints.tokenServices(defaultTokenServices).authenticationManager(authenticationManager);
	}

	/*
	 * @Override public void configure(AuthorizationServerSecurityConfigurer
	 * oauthServer) throws Exception { oauthServer.realm(REALM+"/client"); }
	 */

}