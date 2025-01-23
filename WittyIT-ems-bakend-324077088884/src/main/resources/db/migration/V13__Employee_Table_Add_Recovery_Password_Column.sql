ALTER TABLE `employee` 
ADD COLUMN `recovery_password` VARCHAR(250) NULL COMMENT '' AFTER `password`;
