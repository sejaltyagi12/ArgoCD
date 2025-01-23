ALTER TABLE `designation` 
DROP INDEX `designation_UNIQUE` ;

ALTER TABLE designation 
ADD COLUMN level varchar(150) NOT NULL;

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(1,'Software Trainee','LVL_0',1),(2,'Software Engineer','LVL_1',1),(3,'Software Engineer Level 2','LVL_2',1),(4,'Senior Software Engineer','LVL_3',1),(5,'Team Lead','LVL_4',1),(6,'Project Lead','LVL_5',1),(7,'Project Manager','LVL_6',1),(8,'Senior Project Manager','LVL_7',1),(9,'Senior Software Consultant','LVL_8',1),(10,'Director Engineering','LVL_9',1),(11,'Vice President','LVL_10',1),(12,'CTO','LVL_11',1);

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(13,'UI Trainee','LVL_0',2),(14,'UI Web Developer','LVL_1',2),(15,'UI Web Developer Level 2','LVL_2',2),(16,'UI Senior Web Developer','LVL_3',2);

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(17,'Software Testing Trainee','LVL_0',3),(18,'Software Test Engineer','LVL_1',3),(19,'Software Test Engineer Level 2','LVL_2',3),(20,'Senior Software Test Engineer','LVL_3',3),(21,'Test Lead','LVL_4',3),(22,'Senior Test Lead','LVL_5',3),(23,'Test Manager','LVL_6',3),(24,'Senior Test Manager','LVL_7',3);

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(25,'Operations Trainee','LVL_0',4),(26,'Operations Associate','LVL_1',4),(27,'Manager Operations','LVL_2',4),(28,'Senior Operations Manager','LVL_3',4),(29,'VP','LVL_4',4);

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(30,'HR Trainee','LVL_0',5),(31,'HR Recruiter Associate','LVL_1',5),(32,'HR Generalist Associate','LVL_2',5),(33,'HR Manager','LVL_3',5),(34,'Senior HR Manager','LVL_4',5),(35,'HR Head','LVL_5',5),(36,'VP','LVL_6',5);

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(37,'BD Trainee','LVL_0',6),(38,'BD Associate','LVL_1',6),(39,'BD Manager','LVL_2',6),(40,'Senior BD Manager','LVL_3',6);

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(41,'Front office Trainee','LVL_0',7),(42,'Front office Executive','LVL_1',7),(43,'Front office Manager','LVL_2',7);

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(44,'IT Support Trainee','LVL_0',8),(45,'IT Specialist','LVL_1',8),(46,'IT Manager','LVL_2',8),(47,'Senior IT Manager','LVL_3',8);