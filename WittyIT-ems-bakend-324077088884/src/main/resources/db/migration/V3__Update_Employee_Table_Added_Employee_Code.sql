ALTER TABLE `employee` 
ADD COLUMN `emp_code` VARCHAR(20) NULL COMMENT '' AFTER `id`,
ADD UNIQUE INDEX `emp_code_UNIQUE` (`emp_code` ASC)  COMMENT '';
