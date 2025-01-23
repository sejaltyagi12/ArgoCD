--Add unit column in company_category table.
ALTER TABLE company_category ADD COLUMN unit varchar(100) NOT NULL;

UPDATE company_category
SET unit = 'days',
total_maternity_leave=90
WHERE company_name = 'Wittybrains Software Technologies Pvt. Ltd.';