-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2025 at 08:26 AM
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
-- Database: `car_add`
--

-- --------------------------------------------------------

--
-- Table structure for table `carregistration`
--

CREATE TABLE `carregistration` (
  `reg_no` varchar(120) NOT NULL,
  `brand` varchar(120) NOT NULL,
  `model` varchar(120) NOT NULL,
  `rental` varchar(120) NOT NULL,
  `Available` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carregistration`
--

INSERT INTO `carregistration` (`reg_no`, `brand`, `model`, `rental`, `Available`) VALUES
('ABC-1234', 'HONDA', 'PREUS', '7000', 'Yes'),
('CAR-4545', 'HONDA', 'PREUS', '7400', 'Yes'),
('BAC-5561', 'ZUZUKI', 'MARUTI', '5000', 'Yes');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
