-- Task locked to create this fields for debuging purpose.
ALTER TABLE `leave_account` 
ADD COLUMN `last_modified` DATETIME NULL DEFAULT NULL AFTER `availed_privilege_leave`,
ADD COLUMN `creation_date` DATETIME NULL DEFAULT NULL AFTER `last_modified`;
