--Length validation implemented as asked

ALTER TABLE `employee` 
CHANGE COLUMN `location` `location` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `bank_name` `bank_name` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `official_email` `official_email` VARCHAR(200) NULL DEFAULT NULL ,
CHANGE COLUMN `personal_email` `personal_email` VARCHAR(200) NULL DEFAULT NULL ,
CHANGE COLUMN `present_address` `present_address` VARCHAR(500) NULL DEFAULT NULL ,
CHANGE COLUMN `permanent_address` `permanent_address` VARCHAR(500) CHARACTER SET 'latin1' NULL DEFAULT NULL ,
CHANGE COLUMN `nationality` `nationality` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `hobbies` `hobbies` VARCHAR(200) NULL DEFAULT NULL ;

ALTER TABLE `nominee` 
CHANGE COLUMN `nominee_email` `nominee_email` VARCHAR(200) NULL DEFAULT NULL ,
CHANGE COLUMN `nominee_permanent_address` `nominee_permanent_address` VARCHAR(500) NULL DEFAULT NULL ;
