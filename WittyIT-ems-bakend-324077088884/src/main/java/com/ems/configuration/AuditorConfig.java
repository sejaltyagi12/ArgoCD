package com.ems.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {

    @Bean
    public SpringSecurityAuditorAware auditorProvider(){
        return new SpringSecurityAuditorAware();
    }
}
