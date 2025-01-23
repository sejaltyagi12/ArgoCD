-- MySQL dump 10.13  Distrib 5.6.24, for Win32 (x86)
--
-- Host: localhost    Database: ems
-- ------------------------------------------------------
-- Server version	5.5.38

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `company_category`
--

DROP TABLE IF EXISTS `company_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company_category` (
  `company_id` int(4) NOT NULL,
  `company_name` varchar(150) NOT NULL,
  `company_prefix` varchar(10) NOT NULL,
  `total_sick_leave` int(2) NOT NULL,
  `total_casual_leave` int(2) NOT NULL,
  PRIMARY KEY (`company_id`),
  UNIQUE KEY `company_name_UNIQUE` (`company_name`),
  UNIQUE KEY `company_prefix_UNIQUE` (`company_prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `dept_id` int(4) NOT NULL,
  `dept_name` varchar(100) NOT NULL,
  `id` int(4) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`dept_id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `dept_id_UNIQUE` (`dept_id`),
  UNIQUE KEY `dept_name_UNIQUE` (`dept_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `designation`
--

DROP TABLE IF EXISTS `designation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `designation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `designation` varchar(80) NOT NULL,
  `designation_id` int(11) NOT NULL,
  PRIMARY KEY (`designation_id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `designation_UNIQUE` (`designation`),
  UNIQUE KEY `designation_id_UNIQUE` (`designation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(4) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `gender` varchar(15) NOT NULL,
  `dob` datetime NOT NULL,
  `dept_id` int(4) DEFAULT NULL,
  `middle_name` varchar(80) DEFAULT NULL,
  `last_name` varchar(80) DEFAULT NULL,
  `father_name` varchar(100) DEFAULT NULL,
  `joining_date` datetime NOT NULL,
  `designation_id` int(11) DEFAULT NULL,
  `manager_id` int(11) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `bank_name` varchar(80) DEFAULT NULL,
  `bank_account_number` varchar(50) DEFAULT NULL,
  `bank_ifsc` varchar(45) DEFAULT NULL,
  `official_email` varchar(100) DEFAULT NULL,
  `personal_email` varchar(100) DEFAULT NULL,
  `present_address` varchar(150) DEFAULT NULL,
  `permanent_address` varchar(150) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `primary_phone` varchar(25) DEFAULT NULL,
  `secondary_phone` varchar(25) DEFAULT NULL,
  `pan_number` varchar(25) DEFAULT NULL,
  `uan_number` varchar(50) DEFAULT NULL,
  `aadhar` varchar(50) DEFAULT NULL,
  `blood_group` varchar(3) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `password` varchar(250) NOT NULL,
  `modified_by` varchar(100) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `role_id` int(4) NOT NULL,
  `deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `official_email_UNIQUE` (`official_email`),
  KEY `dept_id_idx` (`dept_id`),
  KEY `designation_id_idx` (`designation_id`),
  KEY `role_id_idx` (`role_id`),
  KEY `company_id_idx` (`company_id`),
  CONSTRAINT `company_id` FOREIGN KEY (`company_id`) REFERENCES `company_category` (`company_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dept_id` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `designation_id` FOREIGN KEY (`designation_id`) REFERENCES `designation` (`designation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `leave_account`
--

DROP TABLE IF EXISTS `leave_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leave_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_id` int(11) NOT NULL,
  `year` int(5) NOT NULL,
  `remaining_casual_leave` float NOT NULL,
  `remaining_sick_leave` float NOT NULL,
  `total_casual_leave` float NOT NULL,
  `total_sick_leave` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `leave_history`
--

DROP TABLE IF EXISTS `leave_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leave_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_id` int(11) NOT NULL,
  `from_date` datetime NOT NULL,
  `to_date` datetime NOT NULL,
  `is_approved` bit(1) NOT NULL,
  `type_id` int(1) NOT NULL,
  `applied_date` datetime NOT NULL,
  `approved_date` datetime DEFAULT NULL,
  `approved_by` int(11) DEFAULT NULL,
  `day_count` float NOT NULL,
  `deductable_day_count` int(2) NOT NULL,
  `employee_comments` varchar(300) DEFAULT NULL,
  `manager_comments` varchar(300) DEFAULT NULL,
  `action_taken` bit(1) NOT NULL,
  `manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `type_id_idx` (`type_id`),
  CONSTRAINT `type_id` FOREIGN KEY (`type_id`) REFERENCES `leave_type` (`type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `leave_type`
--

DROP TABLE IF EXISTS `leave_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leave_type` (
  `type_id` int(1) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` int(4) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  `id` int(4) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `role_name_UNIQUE` (`role_name`),
  UNIQUE KEY `role_id_UNIQUE` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'ems'
--

--
-- Dumping routines for database 'ems'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-24 15:40:30
