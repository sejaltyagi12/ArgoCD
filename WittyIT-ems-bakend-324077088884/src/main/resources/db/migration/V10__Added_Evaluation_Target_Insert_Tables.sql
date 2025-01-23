--
-- Table structure for table `evaluation_cycle`
--

DROP TABLE IF EXISTS `evaluation_cycle`;
CREATE TABLE `evaluation_cycle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `employee_end_date` datetime NOT NULL,
  `manager_end_date` datetime NOT NULL,
  `is_completed` bit(1) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `evaluation_group`
--

DROP TABLE IF EXISTS `evaluation_group`;
CREATE TABLE `evaluation_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `order_index` int(4) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `evaluation_rating`
--

DROP TABLE IF EXISTS `evaluation_rating`;
CREATE TABLE `evaluation_rating` (
  `rating_id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(300) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`rating_id`),
  UNIQUE KEY `description_UNIQUE` (`description`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `evaluation_target_question`
--

DROP TABLE IF EXISTS `evaluation_target_question`;
CREATE TABLE `evaluation_target_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) NOT NULL,
  `group_id` int(4) NOT NULL,
  `rating_id` int(4) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `target_id_idx` (`group_id`),
  KEY `rating_id_idx` (`rating_id`),
  CONSTRAINT `group_id` FOREIGN KEY (`group_id`) REFERENCES `evaluation_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `rating_id` FOREIGN KEY (`rating_id`) REFERENCES `evaluation_rating` (`rating_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `evaluation_target`
--

DROP TABLE IF EXISTS `evaluation_target`;
CREATE TABLE `evaluation_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `target_question_id` int(11) NOT NULL,
  `design_id` int(11) NOT NULL,
  `order_index` int(2) DEFAULT NULL,
  `closure_date` datetime DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `target_question_id_idx` (`target_question_id`),
  KEY `design_id_idx` (`design_id`),
  CONSTRAINT `design_id` FOREIGN KEY (`design_id`) REFERENCES `designation` (`designation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `target_question_id` FOREIGN KEY (`target_question_id`) REFERENCES `evaluation_target_question` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `evaluation_history`
--

DROP TABLE IF EXISTS `evaluation_history`;
CREATE TABLE `evaluation_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) NOT NULL,
  `target_id` int(11) NOT NULL,
  `employee_rating` int(2) DEFAULT NULL,
  `employee_reason` varchar(300) DEFAULT NULL,
  `employee_submitted` bit(1) NOT NULL DEFAULT b'0',
  `employee_submitted_date` datetime DEFAULT NULL,
  `manager_rating` int(2) DEFAULT NULL,
  `manager_reason` varchar(300) DEFAULT NULL,
  `manager_submitted` bit(1) NOT NULL DEFAULT b'0',
  `manager_submitted_date` datetime DEFAULT NULL,
  `cycle_id` int(11) NOT NULL,
  `final_review_by` int(11) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `target_id_idx` (`target_id`),
  KEY `employee_id_idx` (`employee_id`),
  KEY `cycle_id_idx` (`cycle_id`),
  CONSTRAINT `cycle_id` FOREIGN KEY (`cycle_id`) REFERENCES `evaluation_cycle` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `target_id` FOREIGN KEY (`target_id`) REFERENCES `evaluation_target` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




