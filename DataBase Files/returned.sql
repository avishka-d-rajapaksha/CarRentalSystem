-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2025 at 08:28 AM
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
-- Database: `returned`
--

-- --------------------------------------------------------

--
-- Table structure for table `returned_cars`
--

CREATE TABLE `returned_cars` (
  `id` int(11) NOT NULL,
  `vehicle_no` varchar(20) NOT NULL,
  `cust_nic` varchar(20) NOT NULL,
  `rent_date` date NOT NULL,
  `expected_return_date` date NOT NULL,
  `actual_return_date` date NOT NULL,
  `delay_days` int(11) NOT NULL,
  `final_fee` int(11) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `returned_cars`
--

INSERT INTO `returned_cars` (`id`, `vehicle_no`, `cust_nic`, `rent_date`, `expected_return_date`, `actual_return_date`, `delay_days`, `final_fee`, `status`) VALUES
(1, 'BAC-5561', '200104301676', '2025-06-14', '2025-06-18', '2025-06-18', 1, 10000, 'Returned'),
(2, 'ABC-1234', '199856421355', '2025-06-14', '2025-06-15', '2025-06-16', 1, 14000, 'Returned'),
(3, 'CAR-4545', '199856421355', '2025-06-14', '2025-06-16', '2025-06-16', 0, 14800, 'Returned');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `returned_cars`
--
ALTER TABLE `returned_cars`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `returned_cars`
--
ALTER TABLE `returned_cars`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
