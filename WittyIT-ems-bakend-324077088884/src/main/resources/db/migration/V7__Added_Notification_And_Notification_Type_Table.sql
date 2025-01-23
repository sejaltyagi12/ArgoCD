--
-- Table structure for table `notification_type`
--

DROP TABLE IF EXISTS `notification_type`;
CREATE TABLE `notification_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notification_type_id` int(4) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`notification_type_id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Data for table `notification_type`
--

LOCK TABLES `notification_type` WRITE;
INSERT INTO `notification_type` VALUES (1,1,'Birthday'),(2,2,'Admin');
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `text` varchar(250) NOT NULL,
  `notification_type_id` int(4) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  `expiry_date` datetime NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `notification_type_id_idx` (`notification_type_id`),
  CONSTRAINT `notification_type_id` FOREIGN KEY (`notification_type_id`) REFERENCES `notification_type` (`notification_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
