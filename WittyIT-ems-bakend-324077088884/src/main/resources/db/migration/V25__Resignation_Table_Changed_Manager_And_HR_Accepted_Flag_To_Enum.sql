ALTER TABLE `resignation` 
CHANGE COLUMN `hr_public_notes` `hr_public_notes` VARCHAR(250) NULL DEFAULT NULL ,
CHANGE COLUMN `manager_accepted` `manager_status` VARCHAR(50) NOT NULL DEFAULT 'PENDING' ,
CHANGE COLUMN `hr_accepted` `hr_status` VARCHAR(50) NOT NULL DEFAULT 'PENDING' ;
