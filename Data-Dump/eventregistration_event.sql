-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: eventregistration
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` bigint NOT NULL,
  `available_tickets` int NOT NULL,
  `date` date NOT NULL,
  `description` varchar(250) NOT NULL,
  `host` varchar(100) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `price` int NOT NULL,
  `sold_tickets` int NOT NULL,
  `venue` varchar(250) NOT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK751x8cp2x1h1fay38u2p5gpkr` (`category_id`),
  CONSTRAINT `FK751x8cp2x1h1fay38u2p5gpkr` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (11,95,'2025-11-27','asdfgh','ABC Band','1762673152076-file.png','Pop concert',12,4,'singapore',2),(12,145,'2025-11-30','Harry Potter Screening at Marina Bay !!!','HBO','1762675330661-file.webp','Harry Potter Screening',25,5,'Marina Bay, Singapore',1),(180,114,'2025-11-29','Let\'s  enjoy this evening with variety of food from different cultures!!','NS Society Singapore','1762946091210-file.png','PotLuck Party',4,6,'NTU Auditorium, NTU ',7),(204,299,'2025-11-29','Get ready folks for the long-awaited concert of the year!!','TLS band','1763176373813-file.jpg','HipHop Tamizha Concert',30,1,'Marina Bay, Singapore',2),(211,99,'2025-11-21','qwert','qwer band','1763180708289-file.jpg','Music Concert',12,1,'NTU Auditorium, NTU ',2);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-19 13:40:43
