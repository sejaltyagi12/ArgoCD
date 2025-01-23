--For cross month lwp details.
ALTER TABLE `leave_history_details` 
ADD COLUMN `first_month_leave_with_out_pay` FLOAT NOT NULL DEFAULT 0 AFTER `leave_history_detail`;