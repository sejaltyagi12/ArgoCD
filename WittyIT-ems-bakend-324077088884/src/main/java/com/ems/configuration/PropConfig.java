package com.ems.configuration;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PropConfig implements InitializingBean {
	
    private static final Logger logger = LoggerFactory.getLogger(PropConfig.class);
    
    @Value("${database.driver}")
    private String databaseDriver;
    @Value("${database.url}")
    private String databaseUrl;
    @Value("${file.assets}")
    private String assetBasePath;
    @Value("${database.username}")
    private String databaseUsername;
    @Value("${database.password}")
    private String databasePassword;
    @Value("${database.showSql}")
    private boolean showSql;
    @Value("${database.generateDdl}")
    private boolean generateDdl;
    @Value("${database.vendor}")
    private org.springframework.orm.jpa.vendor.Database vendor;
    
    @Value("${application.url}")
    private String applicationUrl;
    @Value("${base.context.assets}")
    private String baseContext;
    
    @Value("${application.client.path}")
    private String clientPath;
    
    @Value("${application.logo.path}")
    private String logoPath;
    
    @Value("${application.client.url}")
    private String clientApplicationUrl;
    
    @Value("${notification.birthday.text}")
    private String birthdayText;
    
    
    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.port}")
    private String mailPort;

    @Value("${mail.user}")
    private String mailUser;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${mail.smtp.ssl.protocols}")
    private String smtpSslProtocols;
    
    @Value("${mail.test.emailId}")
	private String testEmailId;
    
    
    @Value("${mail.hr.user}")
    private String hrMail;
    @Value("${mail.hr.password}")
    private String hrMailPassword;
    @Value("${mail.leave.enabled}")
    private boolean includeLeaveMail;
    @Value("${mail.resignation.enabled}")
    private boolean includeResignationMail;
    @Value("${mail.leave.includecc}")
    private boolean includeCcInLeaveMails;
    @Value("${mail.leave.cclist}")
    private String leaveMailCcList;

	
    /**
     * Allows a set of command to be run after all properties have been set.
     *
     * @throws Exception the exception
     */
    @Override
    public void afterPropertiesSet() throws Exception 
    {
        logger.debug("Application Properties={}", this);

    }
	
    
    /**
     * Get the database driver.
     *
     * @return String the Database driver in use
     */
    public String getDatabaseDriver() 
    {
        return databaseDriver;
    }

    /**
     * Sets  the database driver.
     *
     * @param databaseDriver the new database driver
     */
    public void setDatabaseDriver(String databaseDriver) 
    {
        this.databaseDriver = databaseDriver;
    }

    /**
     * Get the database url.
     *
     * @return String the database url
     */
    public String getDatabaseUrl() 
    {
        return databaseUrl;
    }

    /**
     * Sets  the database url.
     *
     * @param databaseUrl the database url
     */
    public void setDatabaseUrl(String databaseUrl) 
    {
        this.databaseUrl = databaseUrl;
    }

    public String getLogoPath() {
		return logoPath;
	}


	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}


	/**
     * Get the database user name.
     *
     * @return String database user name
     */
    public String getDatabaseUsername() 
    {
        return databaseUsername;
    }

    /**
     * Sets  the database user name.
     *
     * @param databaseUsername user that will access the database
     */
    public void setDatabaseUsername(String databaseUsername) 
    {
        this.databaseUsername = databaseUsername;
    }

    /**
     * Get the database user's password.
     *
     * @return String user's password
     */
    public String getDatabasePassword() 
    {
        return databasePassword;
    }

    /**
     * Sets  the database user's password.
     *
     * @param databasePassword password of the user that will access the database
     */
    public void setDatabasePassword(String databasePassword) 
    {
        this.databasePassword = databasePassword;
    }

    /**
     * Showsql is set?.
     *
     * @return boolean identify is sql statements will be logged on the output or not
     */
    public boolean isShowSql() 
    {
        return showSql;
    }

    /**
     * Sets  the showsql value. 
     * @param showSql true will dump all sql statements to the output, false will suppress this output
     */
    public void setShowSql(boolean showSql) 
    {
        this.showSql = showSql;
    }

    /**
     * Data definistion language is set?.
     *
     * @return boolean state of generate ddl
     */
    public boolean isGenerateDdl() 
    {
        return generateDdl;
    }

    /**
     * Sets  the generateDdl's value.
     *
     * @param generateDdl true will generate ddl commands false otherwise
     */
    public void setGenerateDdl(boolean generateDdl) 
    {
        //this.generateDdl = generateDdl;
    	this.generateDdl = false;
    }

    /**
     * Get the database's vendor.
     *
     * @return DEFAULT,	DB2, DERBY, H2, HSQL, INFORMIX, MYSQL, ORACLE, POSTGRESQL, SQL_SERVER or SYBASE
     */
    public org.springframework.orm.jpa.vendor.Database getVendor() 
    {
        return vendor;
    }

    /**
     * Sets  database's vendor.
     *
     * @param vendor org.springframework.orm.jpa.vendor.Database (DEFAULT,	DB2, DERBY, H2, HSQL, INFORMIX, MYSQL, ORACLE, POSTGRESQL, SQL_SERVER or SYBASE)
     */
    public void setVendor(org.springframework.orm.jpa.vendor.Database vendor) 
    {
        this.vendor = vendor;
    }
    
    /**
	 * @return the assetBasePath
	 */
	public String getAssetBasePath() {
		return assetBasePath;
	}

	/**
	 * @param assetBasePath the assetBasePath to set
	 */
	public void setAssetBasePath(String assetBasePath) {
		this.assetBasePath = assetBasePath;
	}


	public String getApplicationUrl() {
		return applicationUrl;
	}


	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}


	/**
	 * @return the baseContext
	 */
	public String getBaseContext() {
		return baseContext;
	}

	/**
	 * @param baseContext the baseContext to set
	 */
	public void setBaseContext(String baseContext) {
		this.baseContext = baseContext;
	}

	/**
	 * @return the Birthday wish Static Text
	 */
	public String getBirthdayText() {
		return birthdayText;
	}

	/**
	 * @param the Birthday wish Static Text
	 */
	public void setBirthdayText(String birthdayText) {
		this.birthdayText = birthdayText;
	}


	public String getMailHost() {
		return mailHost;
	}


	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}


	public String getMailPort() {
		return mailPort;
	}


	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}


	public String getMailUser() {
		return mailUser;
	}


	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}


	public String getMailPassword() {
		return mailPassword;
	}


	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}


	public String getSmtpSslProtocols() {
		return smtpSslProtocols;
	}


	public void setSmtpSslProtocols(String smtpSslProtocols) {
		this.smtpSslProtocols = smtpSslProtocols;
	}


	public String getTestEmailId() {
		return testEmailId;
	}


	public void setTestEmailId(String testEmailId) {
		this.testEmailId = testEmailId;
	}


	public static Logger getLogger() {
		return logger;
	}


	public String getClientPath() {
		return clientPath;
	}


	public void setClientPath(String clientPath) {
		this.clientPath = clientPath;
	}


	public String getClientApplicationUrl() {
		return clientApplicationUrl;
	}


	public void setClientApplicationUrl(String clientApplicationUrl) {
		this.clientApplicationUrl = clientApplicationUrl;
	}


	public String getHrMail() {
		return hrMail;
	}


	public void setHrMail(String hrMail) {
		this.hrMail = hrMail;
	}


	public String getHrMailPassword() {
		return hrMailPassword;
	}


	public void setHrMailPassword(String hrMailPassword) {
		this.hrMailPassword = hrMailPassword;
	}


	public boolean isIncludeLeaveMail() {
		return includeLeaveMail;
	}


	public void setIncludeLeaveMail(boolean includeLeaveMail) {
		this.includeLeaveMail = includeLeaveMail;
	}
	
	
	public boolean isIncludeResignationMail() {
		return includeResignationMail;
	}


	public void setIncludeResignationMail(boolean includeResignationMail) {
		this.includeResignationMail = includeResignationMail;
	}


	public boolean isIncludeCcInLeaveMails() {
		return includeCcInLeaveMails;
	}


	public void setIncludeCcInLeaveMails(boolean includeCcInLeaveMails) {
		this.includeCcInLeaveMails = includeCcInLeaveMails;
	}

	public String getLeaveMailCcList() {
		return leaveMailCcList;
	}
	
	public List<String> getLeaveMailCcArrayList() {
		return Arrays.asList(leaveMailCcList.split(","));
	}

	public void setLeaveMailCcList(String leaveMailCcList) {
		this.leaveMailCcList = leaveMailCcList;
	}	
}
