ALTER TABLE `notification` 
ADD COLUMN `actual_expiry_date` DATETIME NULL COMMENT '' AFTER `expiry_date`;
