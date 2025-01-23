/*New column added to measure the date when change of level from 0 to other. Used for calulation of PL of previous months in case level changed from 0 to other.*/
ALTER TABLE `employee` 
ADD COLUMN `level_changed_from_zero` DATETIME NULL AFTER `nominee_id`;