-- SQL script to Add "TRANSPORT ALLOWANCES column" in PaySlip table

ALTER TABLE pay_slip 
ADD COLUMN transport_allowances FLOAT;

-- SQL script to Add "loan column" in PaySlip table
ALTER TABLE pay_slip 
ADD COLUMN loan FLOAT;