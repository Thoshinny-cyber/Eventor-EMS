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
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat` (
  `id` bigint NOT NULL,
  `is_seat_booked` bit(1) NOT NULL,
  `seat_number` varchar(255) DEFAULT NULL,
  `event_id` bigint DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3ovym176pe1oxrr87flc9ojcq` (`event_id`),
  KEY `FK8vypjisvjd3p1ecpwymeq95wu` (`order_id`),
  KEY `FKhhjcwkhi8hx9hfl9fygf1jatl` (`user_id`),
  CONSTRAINT `FK3ovym176pe1oxrr87flc9ojcq` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
  CONSTRAINT `FK8vypjisvjd3p1ecpwymeq95wu` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKhhjcwkhi8hx9hfl9fygf1jatl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES (179,_binary '\0','A6',12,178,1),(183,_binary '\0','A3',180,182,1),(185,_binary '\0','F18',180,184,5),(187,_binary '\0','F18',180,186,5),(189,_binary '\0','F18',180,188,5),(191,_binary '\0','F18',180,190,5),(193,_binary '\0','F18',180,192,5),(195,_binary '\0','A8',12,194,5),(197,_binary '\0','A1',11,196,5),(199,_binary '\0','B12',11,198,5),(201,_binary '\0','A4',11,200,5),(203,_binary '\0','A7',11,202,1),(206,_binary '\0','D7',204,205,5),(208,_binary '\0','H8',12,207,5),(209,_binary '\0','H7',12,207,5),(210,_binary '\0','H9',12,207,5),(214,_binary '\0','A1',211,213,5);
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-19 13:40:44
