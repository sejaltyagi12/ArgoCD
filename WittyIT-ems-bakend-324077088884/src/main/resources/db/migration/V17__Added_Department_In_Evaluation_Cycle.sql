ALTER TABLE `evaluation_cycle` 
ADD COLUMN `department_id` INT(4) NOT NULL DEFAULT 1 AFTER `is_completed`,
ADD INDEX `department_id_idx` (`department_id` ASC);
ALTER TABLE `evaluation_cycle` 
ADD CONSTRAINT `department_id`
  FOREIGN KEY (`department_id`)
  REFERENCES `department` (`dept_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  