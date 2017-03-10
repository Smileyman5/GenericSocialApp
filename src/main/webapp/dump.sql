-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: social_data
-- ------------------------------------------------------
-- Server version	5.7.17

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
-- Current Database: `social_data`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `social_data` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `social_data`;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friends` (
  `username` varchar(32) NOT NULL,
  `friend` varchar(32) NOT NULL,
  `status` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`username`,`friend`),
  KEY `friend` (`friend`),
  KEY `status` (`status`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friend`) REFERENCES `users` (`username`),
  CONSTRAINT `friends_ibfk_3` FOREIGN KEY (`status`) REFERENCES `requeststatus` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
INSERT INTO `friends` VALUES ('Dummy','JesGirl','Confirmed'),('Dummy','Smileyman5','Confirmed'),('JesGirl','Dummy','Confirmed'),('JonMan','Smileyman5','Confirmed'),('Smileyman5','Dummy','Confirmed'),('Smileyman5','JonMan','Confirmed'),('JonMan','DaisyDuke','Pending'),('Smileyman5','JesGirl','Pending');
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requeststatus`
--

DROP TABLE IF EXISTS `requeststatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requeststatus` (
  `status` varchar(32) NOT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requeststatus`
--

LOCK TABLES `requeststatus` WRITE;
/*!40000 ALTER TABLE `requeststatus` DISABLE KEYS */;
INSERT INTO `requeststatus` VALUES ('Confirmed'),('Pending');
/*!40000 ALTER TABLE `requeststatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `birthday` varchar(32) DEFAULT NULL,
  `first_name` varchar(32) DEFAULT NULL,
  `last_name` varchar(32) DEFAULT NULL,
  `gender` varchar(32) DEFAULT NULL,
  `login` int(11) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('AlexMan','password',NULL,NULL,NULL,'male',0),('DaisyDuke','password','11/21/1995','Daisy','Duke','female',0),('Dummy','password','12/12/1990','Dum','Me','male',2),('JesGirl','password','06/08/1993','Jess','Smith','female',0),('JonMan','password','02/18/1995','Jon','Baker','male',0),('Smileyman5','password','03/16/1996','Alex','Kouthoofd','male',11);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-07 14:23:48
