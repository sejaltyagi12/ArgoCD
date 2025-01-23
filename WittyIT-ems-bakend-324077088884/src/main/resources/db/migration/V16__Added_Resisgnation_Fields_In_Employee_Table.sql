ALTER TABLE `employee` 
ADD COLUMN `resign_date` DATETIME NULL AFTER `deleted`,
ADD COLUMN `resign_reason` VARCHAR(200) NULL AFTER `resign_date`;