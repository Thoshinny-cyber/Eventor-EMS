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
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `invoice_details` text,
  `invoice_number` varchar(255) NOT NULL,
  `payment_method` varchar(255) NOT NULL,
  `payment_status` varchar(255) NOT NULL,
  `total_amount` double NOT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l1x55mfsay7co0r3m9ynvipd5` (`invoice_number`),
  KEY `FK4ko3y00tkkk2ya3p6wnefjj2f` (`order_id`),
  CONSTRAINT `FK4ko3y00tkkk2ya3p6wnefjj2f` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,'2025-11-13 15:25:44.721541','Event: PotLuck Party\nVenue: NTU Auditorium, NTU \nDate: 2025-11-29\nTickets: 1\nPrice per Ticket: S$4\nTotal Amount: S$4.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3SSuuUC7pfSWHvWH1Y4sE8hi\nPayment Method: Stripe\nStatus: PAID\n','INV-20251113152544-F2A88D','Stripe','PAID',4,192),(2,'2025-11-13 15:57:17.036348','Event: Harry Potter Screening\nVenue: Marina Bay, Singapore\nDate: 2025-11-30\nTickets: 1\nPrice per Ticket: S$25\nTotal Amount: S$25.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3SSvP4C7pfSWHvWH1YEeliha\nPayment Method: Stripe\nStatus: PAID\n','INV-20251113155717-255621','Stripe','PAID',25,194),(3,'2025-11-13 16:34:28.488427','Event: Pop concert\nVenue: singapore\nDate: 2025-11-27\nTickets: 1\nPrice per Ticket: S$12\nTotal Amount: S$12.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3SSvz3C7pfSWHvWH1azv6vlF\nPayment Method: Stripe\nStatus: PAID\n','INV-20251113163428-795CA1','Stripe','PAID',12,196),(4,'2025-11-13 16:47:31.962804','Event: Pop concert\nVenue: singapore\nDate: 2025-11-27\nTickets: 1\nPrice per Ticket: S$12\nTotal Amount: S$12.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3SSwBfC7pfSWHvWH22ham9GM\nPayment Method: Stripe\nStatus: PAID\n','INV-20251113164731-4E6289','Stripe','PAID',12,198),(5,'2025-11-13 17:19:04.677284','Event: Pop concert\nVenue: singapore\nDate: 2025-11-27\nTickets: 1\nPrice per Ticket: S$12\nTotal Amount: S$12.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3SSwgEC7pfSWHvWH1CNSTpgy\nPayment Method: Stripe\nStatus: PAID\n','INV-20251113171904-851286','Stripe','PAID',12,200),(6,'2025-11-15 10:59:02.092169','Event: Pop concert\nVenue: singapore\nDate: 2025-11-27\nTickets: 1\nPrice per Ticket: S$12\nTotal Amount: S$12.0\nCustomer: user\nEmail: user@example.com\nPayment Intent: pi_3STZhQC7pfSWHvWH1ddpoqcy\nPayment Method: Stripe\nStatus: PAID\n','INV-20251115105901-067862','Stripe','PAID',12,202),(7,'2025-11-15 11:14:05.983072','Event: HipHop Tamizha Concert\nVenue: Marina Bay, Singapore\nDate: 2025-11-29\nTickets: 1\nPrice per Ticket: S$30\nTotal Amount: S$30.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3STZw4C7pfSWHvWH2qKXoeVb\nPayment Method: Stripe\nStatus: PAID\n','INV-20251115111405-A01D7F','Stripe','PAID',30,205),(8,'2025-11-15 11:25:00.398428','Event: Harry Potter Screening\nVenue: Marina Bay, Singapore\nDate: 2025-11-30\nTickets: 3\nPrice per Ticket: S$25\nTotal Amount: S$75.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3STa6gC7pfSWHvWH03tpjsRt\nPayment Method: Stripe\nStatus: PAID\n','INV-20251115112500-5F5994','Stripe','PAID',75,207),(9,'2025-11-15 12:27:50.377869','Event: Music Concert\nVenue: NTU Auditorium, NTU \nDate: 2025-11-21\nTickets: 1\nPrice per Ticket: S$12\nTotal Amount: S$12.0\nCustomer: Thosh\nEmail: thoshbala@gmail.com\nPayment Intent: pi_3STb5UC7pfSWHvWH1A9O7HE5\nPayment Method: Stripe\nStatus: PAID\n','INV-20251115122750-30A73A','Stripe','PAID',12,213);
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-19 13:40:42
