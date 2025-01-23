-- Inserting the wittybrains company category.
INSERT IGNORE INTO `company_category` VALUES (1,'Wittybrains Software Technologies Pvt. Ltd.','wt',8,8,90,15,15,90,5,7,0.5,'days');

-- Inserting two leave types Casual and sick leave.
INSERT IGNORE INTO `leave_type` VALUES (1,'Casual Leave'),(2,'Sick Leave');

-- Inserting roles.
INSERT IGNORE INTO `role` VALUES (1,'EMPLOYEE',1),(2,'ADMIN',2);

-- Default employee added
INSERT IGNORE INTO `employee` VALUES (1,'EMP1',1,'Super','Male','1992-11-11 12:36:13','','Admin','FatherAdmin','2017-01-01 12:36:13',34,1,NULL,'HDFC','1111111111','HDFC2111','admin@witty.com',NULL,'Present address','Permanent Address','9876511111','3243242343','dddd4444f',NULL,NULL,'O+','','$2a$10$gNnwJ9vFljRtXM38wo09Ie9RJHI/uzxZXPnPiqGDhanCwBYAt/f5q','FDQ392SAUPJQMR1A9U4HVNF8GGU8CPCLURV8TQOI',1,NULL,'2018-07-23 12:36:13',NULL,2,'\0',NULL,NULL,'','Indian','Hindu',NULL,'Single',NULL,'2017-01-01 12:36:13');
