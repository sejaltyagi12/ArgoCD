ALTER TABLE `leave_history` 
ADD COLUMN `status` VARCHAR(50) NOT NULL DEFAULT 'PENDING';

UPDATE `leave_history` SET `status`='REJECTED' WHERE `action_taken`='1' AND `is_approved`='0';
UPDATE `leave_history` SET `status`='APPROVED' WHERE `action_taken`='1' AND `is_approved`='1';

ALTER TABLE `leave_history` 
CHANGE COLUMN `is_approved` `is_approved` BIT(1) NOT NULL DEFAULT 0 ,
CHANGE COLUMN `action_taken` `action_taken` BIT(1) NOT NULL DEFAULT 0 ;
