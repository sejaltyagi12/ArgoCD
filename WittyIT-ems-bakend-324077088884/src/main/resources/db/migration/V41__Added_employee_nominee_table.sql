-- SQL script to create "Nominee" Table
CREATE TABLE `nominee` (
`nominee_id` bigint(20) NOT NULL AUTO_INCREMENT,
`nominee_name` varchar(100),
`nominee_relation` varchar(100),
`nominee_phone` varchar(100),
`nominee_email` varchar(100),
`nominee_permanent_address` varchar(100),
  PRIMARY KEY (`nominee_id`)
 );
 
-- SQL script to Add "nominee id as a foreign key in employee" table

ALTER TABLE employee 
ADD COLUMN nominee_id bigint(20);

ALTER TABLE employee 
ADD CONSTRAINT fk_employee_nominee_id FOREIGN KEY (nominee_id) REFERENCES nominee(nominee_id);