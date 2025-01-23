
ALTER TABLE `leave_account` CHANGE COLUMN `remaining_casual_leave` `availed_casual_leave` float NOT NULL;

ALTER TABLE `leave_account` CHANGE COLUMN `remaining_sick_leave` `availed_sick_leave` float NOT NULL;


ALTER TABLE leave_account
ADD COLUMN total_privilege_leave float NOT NULL;

ALTER TABLE leave_account
ADD COLUMN availed_privilege_leave float NOT NULL;
