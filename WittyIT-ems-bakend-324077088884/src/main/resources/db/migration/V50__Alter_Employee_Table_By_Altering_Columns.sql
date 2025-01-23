ALTER TABLE `employee` 
CHANGE COLUMN `modified_by` `modified_by` BIGINT(20) NULL DEFAULT NULL ,
CHANGE COLUMN `created_by` `created_by` BIGINT(20) NULL DEFAULT NULL ;