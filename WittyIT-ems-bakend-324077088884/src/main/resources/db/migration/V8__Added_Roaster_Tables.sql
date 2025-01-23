--
-- Table structure for table `roaster_type`
--

DROP TABLE IF EXISTS `roaster_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roaster_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roaster_type_id` int(4) NOT NULL,
  `type` varchar(40) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roaster_type`
--

LOCK TABLES `roaster_type` WRITE;
INSERT INTO `roaster_type` VALUES (1,1,'DS','Day Shift'),(2,2,'NS','Night Shift'),(3,3,'C','Comp Off'),(4,4,'OFF','Day Off'),(5,5,'GEN','General'),(6,6,'Leave','Leave');
UNLOCK TABLES;

--
-- Table structure for table `roaster_history`
--

DROP TABLE IF EXISTS `roaster_history`;
CREATE TABLE `roaster_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roaster_date` datetime NOT NULL,
  `emp_id` int(11) NOT NULL,
  `roaster_type_id` int(4) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `roaster_type_id_idx` (`roaster_type_id`),
  CONSTRAINT `roaster_type_id` FOREIGN KEY (`roaster_type_id`) REFERENCES `roaster_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
