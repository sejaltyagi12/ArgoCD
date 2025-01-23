ALTER TABLE `leave_history` 
ADD COLUMN `rejected_date` DATETIME NULL DEFAULT NULL AFTER `status`,
ADD COLUMN `withdrawn_date` DATETIME NULL DEFAULT NULL AFTER `rejected_date`;