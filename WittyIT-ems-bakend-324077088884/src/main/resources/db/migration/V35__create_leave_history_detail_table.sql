-- SQL script to create "asset_status" table

CREATE TABLE `leave_history_details` (
`leave_history_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
`year` int NOT NULL,
`deducted_casual_leave` float NOT NULL,
`deducted_privilege_leave` float NOT NULL,
`deducted_sick_leave` float NOT NULL,
`deducted_leave_with_out_pay` float NOT NULL,
 PRIMARY KEY (`leave_history_detail_id`)
);

ALTER TABLE leave_history_details 
ADD COLUMN leave_history_detail int;

ALTER TABLE leave_history_details 
ADD CONSTRAINT fk_leave_history_details_leave_history_detail FOREIGN KEY (leave_history_detail) REFERENCES leave_history(id);