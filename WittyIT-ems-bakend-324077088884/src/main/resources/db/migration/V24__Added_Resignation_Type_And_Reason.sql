
CREATE TABLE `resignation_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id`));
  
 INSERT INTO `resignation_type` (`type`) VALUES ('Resigned');
 
 
 CREATE TABLE `resignation_reason` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `reason` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id`));
  
 INSERT INTO `resignation_reason` (`reason`) VALUES ('Promotion');


ALTER TABLE `resignation` 
ADD COLUMN `manager_public_notes` VARCHAR(250) NULL,
ADD COLUMN `manager_private_notes` VARCHAR(250) NULL,
ADD COLUMN `last_day` DATETIME NOT NULL,
ADD COLUMN `hr_private_notes` VARCHAR(250) NULL,
ADD COLUMN `hr_public_notes` VARCHAR(45) NULL,
ADD COLUMN `manager_accepted` BIT(1) NOT NULL DEFAULT 0,
ADD COLUMN `hr_accepted` BIT(1) NOT NULL DEFAULT 0,
ADD COLUMN `reason_id` INT NOT NULL DEFAULT 1,
ADD COLUMN `resign_type_id` INT NOT NULL DEFAULT 1,
ADD INDEX `resign_type_id_idx` (`resign_type_id` ASC),
ADD INDEX `reason_id_idx` (`reason_id` ASC);
ALTER TABLE `resignation` 
ADD CONSTRAINT `resign_type_id`
  FOREIGN KEY (`resign_type_id`)
  REFERENCES `resignation_type` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `reason_id`
  FOREIGN KEY (`reason_id`)
  REFERENCES `resignation_reason` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  
ALTER TABLE `company_category` 
ADD COLUMN `notice_period` INT(4) NOT NULL DEFAULT 30;

UPDATE `company_category` SET `notice_period`='90' WHERE `company_id`='1';

