-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Feb 18, 2023 at 06:49 PM
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

--
-- Dumping data for table `BasketItem`
--

INSERT INTO `BasketItem` (`product`, `user`, `amount`) VALUES
(1, 2, 1),
(2, 2, 10);

-- --------------------------------------------------------

--
-- Table structure for table `Order`
--

CREATE TABLE `Order` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Order`
--

INSERT INTO `Order` (`id`, `date`, `user`) VALUES
(1, '2023-01-16', 1),
(2, '2023-01-23', 1),
(3, '2023-02-06', 4);

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

--
-- Dumping data for table `OrderItem`
--

INSERT INTO `OrderItem` (`id`, `amount`, `price`, `product`, `order`) VALUES
(1, 1, '6.00', 1, 1),
(2, 5, '10.00', 2, 2),
(3, 3, '6.00', 1, 2),
(5, 3, '1.00', 7, 3);

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

--
-- Dumping data for table `Product`
--

INSERT INTO `Product` (`id`, `price`, `name`, `available`, `description`, `seller`) VALUES
(1, '6.00', 'Chainsaw Man, Vol. 1', 1, 'Denji was a small-time devil hunter just trying to survive in a harsh world. After being killed on a job, he is revived by his pet devil Pochita and becomes something new and dangerous—Chainsaw Man!\r\n\r\nDenji’s a poor young man who’ll do anything for money, even hunting down devils with his pet devil Pochita. He’s a simple man with simple dreams, drowning under a mountain of debt. But his sad life gets turned upside down one day when he’s betrayed by someone he trusts. Now with the power of a devil inside him, Denji’s become a whole new man—Chainsaw Man!', 1),
(2, '2.00', 'Banana', 40, '- Product of Ecuador, we ended a gouverment for this.', 3),
(3, '5.99', 'Chainsaw Man, Vol. 2', 2, 'Denji was a small-time devil hunter just trying to survive in a harsh world. After being killed on a job, he is revived by his pet devil Pochita and becomes something new and dangerous—Chainsaw Man!\r\n\r\nIn order to achieve the greatest goal in human history—to touch a boob—Denji will risk everything in a fight against the dangerous Bat Devil. But will getting what he wants actually make him happy…?', 1),
(4, '6.99', 'Spy x Family, Vol. 3', 1, '<b>An action-packed comedy about a fake family that includes a spy, an assassin and a telepath!</b>\r\n\r\nMaster spy Twilight is unparalleled when it comes to going undercover on dangerous missions for the betterment of the world. But when he receives the ultimate assignment—to get married and have a kid—he may finally be in over his head!\r\n\r\nTwilight has overcome many challenges in putting together the Forger family, but now all his hard work might come undone when Yor’s younger brother Yuri pops in for a surprise visit! Can Twilight outsmart Yuri, who actually works for the Ostanian secret service?!', 1),
(5, '3.00', 'banaranab', 1, 'A severe misspelling of Bannana', 3),
(6, '100.00', 'bananoboros', 0, 'The fusion of the bannana and the mythical oroboros.', 3),
(7, '500.00', 'IPhone 7 ', 1000, 'The revolutionary product that changed the way we live.', 4);

-- --------------------------------------------------------

--
-- Table structure for table `Seller`
--

CREATE TABLE `Seller` (
  `id` int(11) NOT NULL,
  `balance` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Seller`
--

INSERT INTO `Seller` (`id`, `balance`) VALUES
(1, '50.00'),
(3, '1000.00'),
(4, '10000000.00');

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

--
-- Dumping data for table `Session`
--

INSERT INTO `Session` (`id`, `until`, `logged_in`, `user`) VALUES
('75G4zFvpnU27Rka1PWdz1me69zXOga13nafdzv8esEiv5GQwJti6cjatB4ggX2RX', '2024-02-06 11:29:08', 1, 1),
('8zZzlu8LObXgJPfBr16bzSEook8z0ju6BWKesiGhGgXQ6SksBCh7FwYmtn0fPWSz', '2024-02-06 11:29:03', 1, 2),
('iRpFrJnGGobN6czJu1PzppYC2oKIgUe4lI1evIDto2khJGTtrX9erbO2T1apMS0T', '2024-02-06 10:19:08', 0, NULL),
('m7rQlDM8K0ISuDNVb8YcmHyFXI4mDlpB4TlDZQ2nSJtBEMoVQYq2428hz0VN8mv4', '2024-02-06 09:09:27', 0, NULL),
('qvz5s1wGzoXHNQTnIRuSg1kKz7uLlzDubX9ANLbJpcEVedG1jmBO76sWvS6FWVuR', '2024-02-06 09:31:36', 0, NULL),
('W8w4Yw5JGDLwadPojoQNufSgNim01qrRV5wi4JDewMdKT9UnRLazvwlLiX3eA7I6', '2024-02-06 11:30:44', 0, NULL);

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
-- Dumping data for table `User`
--

INSERT INTO `User` (`id`, `firstname`, `lastname`, `email`, `pwdhash`, `salt`, `housenumber`, `street`, `zipcode`, `active`, `activationcode`) VALUES
(1, 'Max', 'Testdorf', 'max@example.com', 'vvV+x/U6bUC+tkCngKY5yDvCmsipgW8fxsXG3Nk8RyE=', '', '13', 'Nordpol Str', '12345', 1, 'lmBoU5CMSll6aX1E33kaq8Gu7CqT1tLdRF7ry533b84HDSqqiY'),
(2, 'Karren', 'Data', 'max@2.example.com', '69c9a06eadcf21373532fa0faf6b06065bfbf8a4d85abac9218acb42c918a118', '', '1', 'Haus-Alee', '51234', 0, 'ZLH89GPubVzkJFvUiUtwkKzzhtTFcd9AYlvlWvifQzOpund1Eb'),
(3, '   ', 'Fruitshop', 'contact@fruitshop.de', '69c9a06eadcf21373532fa0faf6b06065bfbf8a4d85abac9218acb42c918a118', '29da', '20', 'Panama-Str', '88873', 1, 'lkahfjkashkjfhjkfhsjkfhjfhjhgjdghjfjkhgdjhdfjhkghkjfjh'),
(4, '  ', 'Microsoft', 'microsoft@microsoft.com', '69c9a06eadcf21373532fa0faf6b06065bfbf8a4d85abac9218acb42c918a118', 'ajsdkjihfahisf', '0', 'Gate Street', '012345', 1, 'gjhsfhsdhfjdsjkfhdskjhfkjhdsjhkfjkdsjkfjkehuir');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `OrderItem`
--
ALTER TABLE `OrderItem`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `Product`
--
ALTER TABLE `Product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `Seller`
--
ALTER TABLE `Seller`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
  ADD CONSTRAINT `OrderItem_ibfk_1` FOREIGN KEY (`order`) REFERENCES `Order` (`id`),
  ADD CONSTRAINT `OrderItem_ibfk_2` FOREIGN KEY (`product`) REFERENCES `Product` (`id`);

--
-- Constraints for table `Product`
--
ALTER TABLE `Product`
  ADD CONSTRAINT `Product_ibfk_1` FOREIGN KEY (`seller`) REFERENCES `Seller` (`id`);

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
