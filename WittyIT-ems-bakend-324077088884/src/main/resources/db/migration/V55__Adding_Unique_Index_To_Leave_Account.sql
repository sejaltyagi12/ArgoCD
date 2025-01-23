--Adding unique index to the leave account table to prevent two leave account creation of same year for same employee.

ALTER TABLE `leave_account` 
ADD UNIQUE INDEX `UNIQUE` (`emp_id` ASC, `year` ASC);