CREATE TABLE `resignation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `emp_id` INT NOT NULL,
  `manager_id` INT NOT NULL,
  `resignation_text` MEDIUMTEXT NOT NULL,
  `resignation_date` DATETIME NOT NULL,
  `creation_date` DATETIME NULL,
  `created_by` INT NULL,
  `modified_date` DATETIME NULL,
  `modified_by` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `employee_idx` (`emp_id` ASC),
  CONSTRAINT `employee`
    FOREIGN KEY (`emp_id`)
    REFERENCES `employee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
