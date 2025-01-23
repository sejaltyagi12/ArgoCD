ALTER TABLE company_category ADD COLUMN total_privilege_leave bigint(20) NOT NULL;


ALTER TABLE company_category ADD COLUMN total_marriage_leave bigint(20) NOT NULL;


ALTER TABLE company_category ADD COLUMN total_maternity_leave bigint(20) NOT NULL;


ALTER TABLE company_category ADD COLUMN total_paternity_leave bigint(20) NOT NULL;


ALTER TABLE company_category ADD COLUMN total_privilege_leave_carry_forwarder bigint(20) NOT NULL;


ALTER TABLE company_category ADD COLUMN total_civil_duity_leave FLOAT NOT NULL;


UPDATE company_category
SET total_casual_leave = 8,
    total_privilege_leave = 15,
    total_marriage_leave=15,
    total_maternity_leave=3,
    total_paternity_leave=5,
    total_civil_duity_leave=.5,
    total_privilege_leave_carry_forwarder=7
WHERE company_name = 'Wittybrains Software Technologies Pvt. Ltd.';