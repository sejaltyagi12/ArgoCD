ALTER TABLE `pay_slip` 
DROP COLUMN `employee_esi`,
DROP COLUMN `employee_pf`,
CHANGE COLUMN `employer_pf` `pf` FLOAT NULL DEFAULT NULL ,
CHANGE COLUMN `employer_esi` `esi` FLOAT NULL DEFAULT NULL ;