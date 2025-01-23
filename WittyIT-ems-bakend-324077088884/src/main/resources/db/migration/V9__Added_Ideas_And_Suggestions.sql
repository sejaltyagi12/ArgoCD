--
-- Table structure for table `idea`
--

DROP TABLE IF EXISTS `idea`;
CREATE TABLE `idea` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_id` int(11) NOT NULL,
  `manager_id` int(11) NOT NULL,
  `manager_shared` bit(1) NOT NULL,
  `subject` varchar(150) NOT NULL,
  `department_name` varchar(100) NOT NULL,
  `description` mediumtext NOT NULL,
  `is_read` bit(1) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
