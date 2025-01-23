ALTER TABLE `employee` 
DROP FOREIGN KEY `dept_id`;
ALTER TABLE `employee` 
DROP INDEX `dept_id_idx`;

ALTER TABLE`evaluation_cycle` 
DROP FOREIGN KEY `department_id`;
ALTER TABLE `evaluation_cycle` 
DROP INDEX `department_id_idx`;

TRUNCATE TABLE department;

ALTER TABLE `department` 
DROP COLUMN `id`,
DROP INDEX `id_UNIQUE` ;

INSERT INTO `department`(`dept_id`, `dept_name`) VALUES(1,'IT Software'),(2,'UI Designer'),(3,'Testing'),(4,'Admin'),(5,'Human Resource'),(6,'Business Development'),(7,'Front Office'),(8,'IT Support');

ALTER TABLE`evaluation_cycle` ADD CONSTRAINT `department_id` FOREIGN KEY (`department_id`) REFERENCES `department` (`dept_id`);