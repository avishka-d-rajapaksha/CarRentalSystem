-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2025 at 08:27 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `car_rent`
--

-- --------------------------------------------------------

--
-- Table structure for table `rental_records`
--

CREATE TABLE `rental_records` (
  `id` int(11) NOT NULL,
  `cust_nic` varchar(15) NOT NULL,
  `vehicle_no` varchar(15) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `rent_date` date NOT NULL,
  `return_date` date NOT NULL,
  `total_fee` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rental_records`
--

INSERT INTO `rental_records` (`id`, `cust_nic`, `vehicle_no`, `brand`, `rent_date`, `return_date`, `total_fee`) VALUES
(4, '199856421355', 'ABC-1234', 'HONDA', '2025-06-13', '2025-06-14', '7000'),
(12, '200104301676', 'BAC-5561', 'ZUZUKI', '2025-06-14', '2025-06-15', '5000'),
(13, '199856421355', 'ABC-1234', 'HONDA', '2025-06-14', '2025-06-15', '7000'),
(14, '199856421355', 'CAR-4545', 'HONDA', '2025-06-14', '2025-06-16', '14800');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `rental_records`
--
ALTER TABLE `rental_records`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `rental_records`
--
ALTER TABLE `rental_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
