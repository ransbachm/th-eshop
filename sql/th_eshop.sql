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

-- Exportiere Struktur von Tabelle th_eshop.Bestellposition
CREATE TABLE IF NOT EXISTS `Bestellposition` (
                                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                                 `stueckzahl` int(11) NOT NULL,
                                                 `preis` decimal(15,2) NOT NULL,
                                                 `produkt` int(11) NOT NULL,
                                                 `bestellung` int(11) NOT NULL,
                                                 PRIMARY KEY (`id`),
                                                 KEY `bestellung` (`bestellung`),
                                                 KEY `produkt` (`produkt`),
                                                 CONSTRAINT `Bestellposition_ibfk_1` FOREIGN KEY (`bestellung`) REFERENCES `Bestellung` (`id`),
                                                 CONSTRAINT `Bestellposition_ibfk_2` FOREIGN KEY (`produkt`) REFERENCES `Produkt` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- Daten Export vom Benutzer nicht ausgewählt

-- Exportiere Struktur von Tabelle th_eshop.Bestellung
CREATE TABLE IF NOT EXISTS `Bestellung` (
                                            `id` int(11) NOT NULL AUTO_INCREMENT,
                                            `kaufdatum` date NOT NULL,
                                            `nutzer` int(11) NOT NULL,
                                            PRIMARY KEY (`id`),
                                            KEY `nutzer` (`nutzer`),
                                            CONSTRAINT `Bestellung_ibfk_1` FOREIGN KEY (`nutzer`) REFERENCES `Nutzer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Daten Export vom Benutzer nicht ausgewählt

-- Exportiere Struktur von Tabelle th_eshop.LoginToken
CREATE TABLE IF NOT EXISTS `LoginToken` (
                                            `id` int(11) NOT NULL AUTO_INCREMENT,
                                            `wert` varchar(255) NOT NULL,
                                            `gueltig_bis` date NOT NULL DEFAULT (current_timestamp() + interval 1 day),
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Daten Export vom Benutzer nicht ausgewählt

-- Exportiere Struktur von Tabelle th_eshop.Nutzer
CREATE TABLE IF NOT EXISTS `Nutzer` (
                                        `id` int(11) NOT NULL AUTO_INCREMENT,
                                        `vorname` varchar(255) NOT NULL,
                                        `nachname` varchar(255) NOT NULL,
                                        `email` varchar(255) NOT NULL,
                                        `passwort` varchar(255) NOT NULL,
                                        `hausnr` varchar(255) NOT NULL,
                                        `strasse` varchar(255) NOT NULL,
                                        `plz` varchar(255) NOT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Daten Export vom Benutzer nicht ausgewählt

-- Exportiere Struktur von Tabelle th_eshop.Produkt
CREATE TABLE IF NOT EXISTS `Produkt` (
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
                                         `preis` decimal(15,2) NOT NULL,
                                         `bezeichnung` varchar(255) NOT NULL,
                                         `verfuegbar` int(11) NOT NULL,
                                         `verkaeufer` int(11) NOT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `verkaeufer` (`verkaeufer`),
                                         CONSTRAINT `Produkt_ibfk_1` FOREIGN KEY (`verkaeufer`) REFERENCES `Verkaeufer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Daten Export vom Benutzer nicht ausgewählt

-- Exportiere Struktur von Tabelle th_eshop.Verkaeufer
CREATE TABLE IF NOT EXISTS `Verkaeufer` (
                                            `id` int(11) NOT NULL AUTO_INCREMENT,
                                            `vorname` varchar(255) NOT NULL,
                                            `nachname` varchar(255) NOT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Daten Export vom Benutzer nicht ausgewählt

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
