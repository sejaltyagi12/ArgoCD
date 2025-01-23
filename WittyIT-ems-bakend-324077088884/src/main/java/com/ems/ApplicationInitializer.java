package com.ems;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Sets the dev environment and is responsible to run the application in dev environment.
 *
 * @author Avinash Tyagi
 * @version 2.0
 * @since 2014-08-05
 */
@SpringBootApplication
@EnableTransactionManagement
public class ApplicationInitializer extends SpringBootServletInitializer 
{

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    
    /** 
     * Called by SpringBoot framework configure the environment
     * 
     * @param application
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
    {
//        return application.sources(ApplicationInitializer.class).showBanner(false).logStartupInfo(false);
        return application.sources(ApplicationInitializer.class);
    }
    
    /**
     * Main method called when runs the application in dev environment
     * Loads the app profile.
     *
     * @param args the arguments
     */
    public static void main(String[] args) 
    {
        SpringApplication.run(ApplicationInitializer.class, args);
    }

}