package com.ems.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * DatabaseConfig configure the application database based on the property config files.
 *
 * @author Avinash Tyagi
 * @since 2017-03-21
 */
@Configuration
public class DatabaseConfig {

    @Autowired
    protected PropConfig propConfig;

    /**
     * Returns the datasource. Currently it is a DriverManagerDataSource object
     * @return DataSource DriverManagerDataSource object
     */
    @Bean
    public DataSource dataSource() 
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(propConfig.getDatabaseDriver());
        dataSource.setUrl(propConfig.getDatabaseUrl());
        dataSource.setUsername(propConfig.getDatabaseUsername());
        dataSource.setPassword(propConfig.getDatabasePassword());
        
        return dataSource;
    }
    
    /**
     * Return the bean factory validator.
     *
     * @return LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean validator() 
    {
        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        return validator;
    }
    
    /**
     * Entity manager factory.
     *
     * @param dataSource The datasource object. Currently a DriverManagerDataSource object
     * @param jpaVendorAdapter Extra information for vendor's adapter
     * @return LocalContainerEntityManagerFactoryBean The entity manager factory bean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter)
    {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("com.ems.domain");
        //this will autoconfigure joda-time for hibernate.
        lef.getJpaPropertyMap().put("jadira.usertype.autoRegisterUserTypes", "true");
        //this will change how hibernate name tables and columns
        lef.getJpaPropertyMap().put("hibernate.ejb.naming_strategy","org.hibernate.cfg.ImprovedNamingStrategy");
        
        return lef;

    }

    /**
     * The persistence handler to be used. For now it is a Hibernate adapter
     * @return JpaVendorAdapter the persistence adapter (Hibernate for now)
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter()
    {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(propConfig.isShowSql());
        hibernateJpaVendorAdapter.setGenerateDdl(propConfig.isGenerateDdl());
        hibernateJpaVendorAdapter.setDatabase(propConfig.getVendor());
       
        return hibernateJpaVendorAdapter;

    }
    

    /**
     * Get the transaction manager.
     *
     * @return PlatformTransactionManager new instance of JpaTransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager() 
    {
        return new JpaTransactionManager();
    }

}

