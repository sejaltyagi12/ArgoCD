CREATE TABLE `payroll_fixed` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `emp_id` INT NOT NULL,
  `valid_from` DATETIME NOT NULL,
  `expiry` DATETIME NULL,
  `basic_pay` FLOAT NOT NULL,
  `hra` FLOAT NULL,
  `medical_reimbursement` FLOAT NULL,
  `special_allowance` FLOAT NULL,
  `business_promotion` FLOAT NULL,
  `lta` FLOAT NULL,
  `vehicle_reimbursement` FLOAT NULL,
  `attire_allowance` FLOAT NULL,
  `driver_allowance` FLOAT NULL,
  `fixed_bonus` FLOAT NULL,
  `medical_insurance` FLOAT NULL,
  `employer_pf` FLOAT NULL,
  `employee_pf` FLOAT NULL,
  `is_pf_in_percent` BIT(1) NULL,
  `taxes` FLOAT NULL,
  `vehicle_running` FLOAT NULL,
  `tel_reimbursement` FLOAT NULL,
  `employer_esi` FLOAT NULL,
  `employee_esi` FLOAT NULL,
  `modified_date` DATETIME NULL,
  `modified_by` INT NULL,
  `creation_date` DATETIME NULL,
  `created_by` INT NULL,
  `active` BIT(1) NOT NULL DEFAULT 0,
  `deleted` BIT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `employe_idx` (`emp_id` ASC),
  CONSTRAINT `employe`
    FOREIGN KEY (`emp_id`)
    REFERENCES `employee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
	
	CREATE TABLE `payroll_variable` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `emp_id` INT NOT NULL,
  `month` INT(4) NOT NULL,
  `year` INT(6) NOT NULL,
  `taxes` FLOAT NULL,
  `bonus` FLOAT NULL,
  `arrear` FLOAT NULL,
  `other_deductions` FLOAT NULL,
  `other_allowances` FLOAT NULL,
  `modified_date` DATETIME NULL DEFAULT NULL,
  `modified_by` INT(11) NULL DEFAULT NULL,
  `creation_date` DATETIME NULL DEFAULT NULL,
  `created_by` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `employeee_idx` (`emp_id` ASC),
  CONSTRAINT `employeee`
    FOREIGN KEY (`emp_id`)
    REFERENCES `employee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

