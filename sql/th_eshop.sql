-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Feb 26, 2023 at 12:49 PM
-- Server version: 10.10.2-MariaDB-1:10.10.2+maria~ubu2204
-- PHP Version: 8.0.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `th_eshop`
--

-- --------------------------------------------------------

--
-- Table structure for table `BasketItem`
--

CREATE TABLE `BasketItem` (
  `product` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  `amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Order`
--

CREATE TABLE `Order` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `OrderItem`
--

CREATE TABLE `OrderItem` (
  `id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `product` int(11) NOT NULL,
  `order` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Product`
--

CREATE TABLE `Product` (
  `id` int(11) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `name` varchar(255) NOT NULL,
  `available` int(11) NOT NULL,
  `description` text NOT NULL,
  `seller` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Rating`
--

CREATE TABLE `Rating` (
  `user` int(11) NOT NULL,
  `product` int(11) NOT NULL,
  `rating` decimal(2,1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Seller`
--

CREATE TABLE `Seller` (
  `id` int(11) NOT NULL,
  `balance` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Session`
--

CREATE TABLE `Session` (
  `id` varchar(255) NOT NULL,
  `until` timestamp NOT NULL,
  `logged_in` tinyint(1) NOT NULL,
  `user` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `id` int(11) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pwdhash` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `housenumber` varchar(255) NOT NULL,
  `street` varchar(255) NOT NULL,
  `zipcode` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `activationcode` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `BasketItem`
--
ALTER TABLE `BasketItem`
  ADD PRIMARY KEY (`product`,`user`),
  ADD KEY `user` (`user`);

--
-- Indexes for table `Order`
--
ALTER TABLE `Order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user` (`user`) USING BTREE;

--
-- Indexes for table `OrderItem`
--
ALTER TABLE `OrderItem`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order` (`order`) USING BTREE,
  ADD KEY `product` (`product`) USING BTREE;

--
-- Indexes for table `Product`
--
ALTER TABLE `Product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `seller` (`seller`) USING BTREE;

--
-- Indexes for table `Rating`
--
ALTER TABLE `Rating`
  ADD PRIMARY KEY (`user`,`product`),
  ADD KEY `product` (`product`);

--
-- Indexes for table `Seller`
--
ALTER TABLE `Seller`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Session`
--
ALTER TABLE `Session`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user` (`user`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Order`
--
ALTER TABLE `Order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `OrderItem`
--
ALTER TABLE `OrderItem`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Product`
--
ALTER TABLE `Product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Seller`
--
ALTER TABLE `Seller`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `BasketItem`
--
ALTER TABLE `BasketItem`
  ADD CONSTRAINT `BasketItem_ibfk_1` FOREIGN KEY (`product`) REFERENCES `Product` (`id`),
  ADD CONSTRAINT `BasketItem_ibfk_2` FOREIGN KEY (`user`) REFERENCES `User` (`id`);

--
-- Constraints for table `Order`
--
ALTER TABLE `Order`
  ADD CONSTRAINT `Order_ibfk_1` FOREIGN KEY (`user`) REFERENCES `User` (`id`);

--
-- Constraints for table `OrderItem`
--
ALTER TABLE `OrderItem`
  ADD CONSTRAINT `OrderItem_ibfk_1` FOREIGN KEY (`order`) REFERENCES `Order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `OrderItem_ibfk_2` FOREIGN KEY (`product`) REFERENCES `Product` (`id`);

--
-- Constraints for table `Product`
--
ALTER TABLE `Product`
  ADD CONSTRAINT `Product_ibfk_1` FOREIGN KEY (`seller`) REFERENCES `Seller` (`id`);

--
-- Constraints for table `Rating`
--
ALTER TABLE `Rating`
  ADD CONSTRAINT `Rating_ibfk_1` FOREIGN KEY (`user`) REFERENCES `User` (`id`),
  ADD CONSTRAINT `Rating_ibfk_2` FOREIGN KEY (`product`) REFERENCES `Product` (`id`);

--
-- Constraints for table `Seller`
--
ALTER TABLE `Seller`
  ADD CONSTRAINT `Seller_ibfk_1` FOREIGN KEY (`id`) REFERENCES `User` (`id`);

--
-- Constraints for table `Session`
--
ALTER TABLE `Session`
  ADD CONSTRAINT `Session_ibfk_1` FOREIGN KEY (`user`) REFERENCES `User` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
