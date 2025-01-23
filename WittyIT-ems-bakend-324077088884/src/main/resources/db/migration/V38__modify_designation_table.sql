
ALTER TABLE `employee` 
DROP FOREIGN KEY `designation_id`;
ALTER TABLE`employee` 
DROP INDEX `designation_id_idx` ;

ALTER TABLE `evaluation_target` 
DROP FOREIGN KEY `design_id`;
ALTER TABLE `evaluation_target` 
DROP INDEX `design_id_idx` ;

TRUNCATE TABLE designation;

UPDATE `employee` SET `designation_id`=null;

ALTER TABLE `designation` 
DROP COLUMN `id`,
DROP INDEX `id_UNIQUE`;

ALTER TABLE`employee` ADD CONSTRAINT `designation_id` FOREIGN KEY (`designation_id`) REFERENCES `designation` (`designation_id`);

ALTER TABLE`evaluation_target` ADD CONSTRAINT `design_id` FOREIGN KEY (`design_id`) REFERENCES `designation` (`designation_id`);

ALTER TABLE designation 
ADD COLUMN dept_id INT(4) NOT NULL;

ALTER TABLE`designation` ADD CONSTRAINT `dept_id` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`);