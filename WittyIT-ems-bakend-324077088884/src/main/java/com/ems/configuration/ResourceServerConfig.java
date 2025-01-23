package com.ems.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired TokenStore tokenStore;
    @Autowired TokenEnhancer tokenEnhancer;

    @Override
    public void configure(HttpSecurity http) throws Exception 
    {
    	 http.csrf().disable()
        .authorizeRequests()
	  	.antMatchers("/**/oauth/token").permitAll()
	  	//.antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
	  	.antMatchers("/**/password/recovery/email").permitAll()
	  	.antMatchers("/**/password/recovery/reset").permitAll()
	  	.anyRequest().authenticated();
    }

    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() 
    {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setTokenEnhancer(tokenEnhancer);
        defaultTokenServices.setReuseRefreshToken(false);
        return defaultTokenServices;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception
    {  	
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setTokenEnhancer(tokenEnhancer);
        defaultTokenServices.setSupportRefreshToken(false);
        resources.tokenServices(defaultTokenServices).resourceId("com.ems");
    }
}

