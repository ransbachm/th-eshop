-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server Version:               10.9.3-MariaDB-1:10.9.3+maria~ubu2204 - mariadb.org binary distribution
-- Server Betriebssystem:        debian-linux-gnu
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Exportiere Datenbank Struktur für th_eshop
CREATE DATABASE IF NOT EXISTS `th_eshop` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `th_eshop`;

-- Exportiere Struktur von Tabelle th_eshop.Order
CREATE TABLE IF NOT EXISTS `Order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user` (`user`) USING BTREE,
  CONSTRAINT `Order_ibfk_1` FOREIGN KEY (`user`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Exportiere Daten aus Tabelle th_eshop.Order: ~2 rows (ungefähr)
INSERT INTO `Order` (`id`, `date`, `user`) VALUES
	(1, '2023-01-16', 1),
	(2, '2023-01-23', 1);

-- Exportiere Struktur von Tabelle th_eshop.OrderItem
CREATE TABLE IF NOT EXISTS `OrderItem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` int(11) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `product` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order` (`order`) USING BTREE,
  KEY `product` (`product`) USING BTREE,
  CONSTRAINT `OrderItem_ibfk_1` FOREIGN KEY (`order`) REFERENCES `Order` (`id`),
  CONSTRAINT `OrderItem_ibfk_2` FOREIGN KEY (`product`) REFERENCES `Product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- Exportiere Daten aus Tabelle th_eshop.OrderItem: ~3 rows (ungefähr)
INSERT INTO `OrderItem` (`id`, `amount`, `price`, `product`, `order`) VALUES
	(1, 1, 6.00, 1, 1),
	(2, 5, 10.00, 2, 2),
	(3, 3, 6.00, 1, 2);

-- Exportiere Struktur von Tabelle th_eshop.Product
CREATE TABLE IF NOT EXISTS `Product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` decimal(15,2) NOT NULL,
  `name` varchar(255) NOT NULL,
  `available` int(11) NOT NULL,
  `seller` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `seller` (`seller`) USING BTREE,
  CONSTRAINT `Product_ibfk_1` FOREIGN KEY (`seller`) REFERENCES `Seller` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- Exportiere Daten aus Tabelle th_eshop.Product: ~4 rows (ungefähr)
INSERT INTO `Product` (`id`, `price`, `name`, `available`, `seller`) VALUES
	(1, 6.00, 'CSM VOL 11', 1, 1),
	(2, 10.00, 'Banana', 40, 2),
	(3, 5.99, 'CSM VOL 12', 2, 1),
	(4, 5.99, 'CSM VOL 13', 1, 1);

-- Exportiere Struktur von Tabelle th_eshop.Seller
CREATE TABLE IF NOT EXISTS `Seller` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Exportiere Daten aus Tabelle th_eshop.Seller: ~2 rows (ungefähr)
INSERT INTO `Seller` (`id`, `firstname`, `lastname`) VALUES
	(1, 'Hano', 'Takt'),
	(2, 'Renate', 'Vordorf');

-- Exportiere Struktur von Tabelle th_eshop.User
CREATE TABLE IF NOT EXISTS `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pwdhash` varchar(255) NOT NULL,
  `housenumber` varchar(255) NOT NULL,
  `street` varchar(255) NOT NULL,
  `zipcode` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `activationcode` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4;

-- Exportiere Daten aus Tabelle th_eshop.User: ~3 rows (ungefähr)
INSERT INTO `User` (`id`, `firstname`, `lastname`, `email`, `pwdhash`, `housenumber`, `street`, `zipcode`, `active`, `activationcode`) VALUES
	(1, 'Max', 'Testdorf', 'max@example.com', 'vvV+x/U6bUC+tkCngKY5yDvCmsipgW8fxsXG3Nk8RyE=', '13', 'Nordpol Str', '12345', 1, 'lmBoU5CMSll6aX1E33kaq8Gu7CqT1tLdRF7ry533b84HDSqqiY'),
	(2, 'Karren', 'Data', 'max@2.example.com', '69c9a06eadcf21373532fa0faf6b06065bfbf8a4d85abac9218acb42c918a118', '1', 'Haus-Alee', '51234', 1, 'ZLH89GPubVzkJFvUiUtwkKzzhtTFcd9AYlvlWvifQzOpund1Eb'),
	(46, 'A', 'BB', 'ransbachm@gmail.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', '4', 'Abc Street', '12345', 1, 'pwjlfMU0ZFSVmncjWgaEgnde5eq3MUh8');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
