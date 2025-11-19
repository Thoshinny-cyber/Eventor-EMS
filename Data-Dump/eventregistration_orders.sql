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
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL,
  `count` int NOT NULL,
  `event_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `payment_intent_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmg5djn8knpgswktdindbd3uj0` (`event_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKmg5djn8knpgswktdindbd3uj0` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (178,1,12,1,'PAID',NULL),(182,1,180,1,'PAID',NULL),(184,1,180,5,'PAID','pi_3SSuX9C7pfSWHvWH2DzWXQgm'),(186,1,180,5,'PAID','pi_3SSumLC7pfSWHvWH0nYc0tJD'),(188,1,180,5,'PAID','pi_3SSuorC7pfSWHvWH2aQKIBUr'),(190,1,180,5,'PAID','pi_3SSuqeC7pfSWHvWH2RUh7y7r'),(192,1,180,5,'PAID','pi_3SSuuUC7pfSWHvWH1Y4sE8hi'),(194,1,12,5,'PAID','pi_3SSvP4C7pfSWHvWH1YEeliha'),(196,1,11,5,'PAID','pi_3SSvz3C7pfSWHvWH1azv6vlF'),(198,1,11,5,'PAID','pi_3SSwBfC7pfSWHvWH22ham9GM'),(200,1,11,5,'PAID','pi_3SSwgEC7pfSWHvWH1CNSTpgy'),(202,1,11,1,'PAID','pi_3STZhQC7pfSWHvWH1ddpoqcy'),(205,1,204,5,'PAID','pi_3STZw4C7pfSWHvWH2qKXoeVb'),(207,3,12,5,'PAID','pi_3STa6gC7pfSWHvWH03tpjsRt'),(213,1,211,5,'PAID','pi_3STb5UC7pfSWHvWH1A9O7HE5');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
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
