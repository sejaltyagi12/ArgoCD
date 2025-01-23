ALTER TABLE employee
ADD COLUMN login_attempts INT default 0,
ADD COLUMN last_failed_login_attempt datetime,
ADD COLUMN account_unlock_time datetime;