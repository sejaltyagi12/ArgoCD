package com.ems.configuration;


import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.ems.exception.ServiceException;

/**
 * E-Mail Configuration class.
 *
 * @author Andre Pereira
 * @version 2.0
 * @since 2014-08-05
 */
@Configuration
@EnableAsync
public class MailConfig {

    @Autowired
    PropConfig propConfig;

    /**
     * Returns the mail sender object responsible to physically send e-mails.
     *
     * @return JavaMailSender return the JavaMailSender implementation object
     */
    @Bean
    public JavaMailSender mailSender() throws ServiceException{
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        int port = Integer.valueOf(propConfig.getMailPort());
        String host = propConfig.getMailHost();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(propConfig.getMailUser());
        mailSender.setPassword(propConfig.getMailPassword());

        Properties properties = new Properties();
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.protocols", propConfig.getSmtpSslProtocols());
        
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

    /**
     * Get the template loader resolver. Folder is set to: static/html/mail/
     * @return ClassLoaderTemplateResolver responsible to locate and resolve e-mail templates
     */
    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("static/html/mail/");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);
        return templateResolver;
    }
    
    /**
     * Get the e-mail template engine.
     *
     * @return TemplateEngine the e-mail template processor
     */
    @Bean
    public TemplateEngine emailTemplateEngine() {
    	TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(emailTemplateResolver());
        return engine;
    }

}
