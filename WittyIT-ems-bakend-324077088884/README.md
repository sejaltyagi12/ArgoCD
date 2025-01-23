Steps to deploy,
1. Update Mysql version in pom.xml file 
2. Create the build (target/ems.war file) by the command "mvn install". Commandline maven should be installed before this step.
3. Copy that ems.war file inside "${tomcat_home}/webapps/"
4. Take a backup of "${tomcat_home}/webapps/ems/WEB-INF/classes/application.properties"
5. Delete "${tomcat_home}/webapps/ems" folder.
6. Use "jar -xvf ${tomcat_home}/webapps/ems.war" to extract the war file. A new "${tomcat_home}/webapps/ems" folder will be created.
7. Copy "application.properties" file (From Step 3) to "${tomcat_home}/webapps/ems/WEB-INF/classes/" folder. 
8. Start the tomcat again.