-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 29, 2025 at 03:56 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `car_rental`
--

-- --------------------------------------------------------

--
-- Stand-in structure for view `booked_now`
-- (See below for the actual view)
--
CREATE TABLE `booked_now` (
`id` int(11)
,`reg_no` varchar(120)
,`customer_name` varchar(100)
,`start_date` date
,`end_date` date
,`status` enum('Booked','Collected','Completed','Cancelled')
);

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--
-- Creation: Aug 19, 2025 at 10:10 AM
--

CREATE TABLE `bookings` (
  `id` int(11) NOT NULL,
  `reg_no` varchar(120) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `customer_phone` varchar(30) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` enum('Booked','Collected','Completed','Cancelled') NOT NULL DEFAULT 'Booked',
  `notes` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `brand`
--
-- Creation: Aug 17, 2025 at 12:07 PM
--

CREATE TABLE `brand` (
  `id` int(11) NOT NULL,
  `name` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `brand`
--

INSERT INTO `brand` (`id`, `name`) VALUES
(8, 'AUDI'),
(7, 'BMW'),
(12, 'CIVIC'),
(5, 'FORD'),
(11, 'FZS'),
(1, 'HONDA'),
(19, 'HYUNDAI'),
(9, 'KIA'),
(20, 'MAZDA'),
(21, 'MERCEDES-BENZ'),
(4, 'NISSAN'),
(10, 'SUZUKI'),
(6, 'TESLA'),
(3, 'TOYOTA');

-- --------------------------------------------------------

--
-- Table structure for table `car`
--
-- Creation: Aug 19, 2025 at 09:57 AM
--

CREATE TABLE `car` (
  `reg_no` varchar(120) NOT NULL,
  `model_id` int(11) NOT NULL,
  `rental` int(11) NOT NULL,
  `status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `car`
--

INSERT INTO `car` (`reg_no`, `model_id`, `rental`, `status_id`) VALUES
('AUD-2015-09', 218, 9200, 5),
('AUD-2016-09', 219, 9200, 5),
('AUD-2017-09', 220, 9200, 5),
('AUD-2018-09', 221, 9200, 5),
('AUD-2019-09', 222, 9200, 5),
('AUD-2020-09', 22, 9200, 5),
('AUD-2021-09', 223, 9200, 5),
('AUD-2022-09', 224, 9200, 5),
('AUD-2023-09', 225, 9200, 5),
('AUD-2024-09', 226, 9200, 5),
('BMW-2015-08', 209, 8800, 1),
('BMW-2016-08', 210, 8800, 1),
('BMW-2017-08', 211, 8800, 1),
('BMW-2018-08', 212, 8800, 1),
('BMW-2019-08', 213, 8800, 1),
('BMW-2020-08', 214, 8800, 1),
('BMW-2021-08', 7, 8800, 1),
('BMW-2022-08', 215, 8800, 1),
('BMW-2023-08', 216, 8800, 1),
('BMW-2024-08', 217, 8800, 1),
('FRD-2015-07', 199, 8400, 5),
('FRD-2016-07', 200, 8400, 5),
('FRD-2017-07', 201, 8400, 5),
('FRD-2018-07', 202, 8400, 5),
('FRD-2019-07', 203, 8400, 5),
('FRD-2020-07', 204, 8400, 5),
('FRD-2021-07', 205, 8400, 5),
('FRD-2022-07', 206, 8400, 5),
('FRD-2023-07', 207, 8400, 5),
('FRD-2024-07', 208, 8400, 5),
('HND-2015-02', 153, 6400, 1),
('HND-2016-02', 154, 6400, 1),
('HND-2017-02', 155, 6400, 1),
('HND-2018-02', 156, 6400, 1),
('HND-2019-02', 2, 6400, 1),
('HND-2020-02', 157, 6400, 1),
('HND-2021-02', 158, 6400, 1),
('HND-2022-02', 159, 6400, 1),
('HND-2023-02', 160, 6400, 1),
('HND-2024-02', 161, 6400, 1),
('HYU-2015-05', 180, 7600, 5),
('HYU-2016-05', 181, 7600, 5),
('HYU-2017-05', 182, 7600, 5),
('HYU-2018-05', 183, 7600, 5),
('HYU-2019-05', 184, 7600, 5),
('HYU-2020-05', 185, 7600, 5),
('HYU-2021-05', 186, 7600, 5),
('HYU-2022-05', 187, 7600, 5),
('HYU-2023-05', 188, 7600, 5),
('HYU-2024-05', 189, 7600, 5),
('KIA-2015-06', 190, 9500, 1),
('KIA-2016-06', 191, 9500, 1),
('KIA-2017-06', 192, 9500, 1),
('KIA-2018-06', 193, 9500, 1),
('KIA-2019-06', 194, 9500, 1),
('KIA-2020-06', 9, 9500, 1),
('KIA-2021-06', 195, 9500, 1),
('KIA-2022-06', 196, 9500, 1),
('KIA-2023-06', 197, 9500, 1),
('KIA-2024-06', 198, 9500, 1),
('MBZ-2015-10', 227, 9600, 1),
('MBZ-2016-10', 228, 9600, 1),
('MBZ-2017-10', 229, 9600, 1),
('MBZ-2018-10', 230, 9600, 1),
('MBZ-2019-10', 24, 9600, 1),
('MBZ-2020-10', 231, 9600, 1),
('MBZ-2021-10', 232, 9600, 1),
('MBZ-2022-10', 233, 9600, 1),
('MBZ-2023-10', 234, 9600, 1),
('MBZ-2024-10', 235, 9600, 1),
('NIS-2015-03', 162, 9800, 5),
('NIS-2016-03', 163, 9800, 5),
('NIS-2017-03', 164, 9800, 5),
('NIS-2018-03', 165, 9800, 5),
('NIS-2019-03', 166, 9800, 5),
('NIS-2020-03', 3, 9800, 5),
('NIS-2021-03', 167, 9800, 5),
('NIS-2022-03', 168, 9800, 5),
('NIS-2023-03', 169, 9800, 5),
('NIS-2024-03', 170, 9800, 5),
('SUZ-2015-04', 171, 7200, 1),
('SUZ-2016-04', 172, 7200, 1),
('SUZ-2017-04', 173, 7200, 1),
('SUZ-2018-04', 25, 7200, 1),
('SUZ-2019-04', 174, 7200, 1),
('SUZ-2020-04', 175, 7200, 1),
('SUZ-2021-04', 176, 7200, 1),
('SUZ-2022-04', 177, 7200, 1),
('SUZ-2023-04', 178, 7200, 1),
('SUZ-2024-04', 179, 7200, 1),
('TYT-2015-01', 144, 6000, 5),
('TYT-2016-01', 145, 6000, 5),
('TYT-2017-01', 146, 6000, 5),
('TYT-2018-01', 1, 6000, 5),
('TYT-2019-01', 147, 6000, 5),
('TYT-2020-01', 148, 6000, 5),
('TYT-2021-01', 149, 6000, 5),
('TYT-2022-01', 150, 6000, 5),
('TYT-2023-01', 151, 6000, 5),
('TYT-2024-01', 152, 6000, 5);

-- --------------------------------------------------------

--
-- Table structure for table `carregistration`
--
-- Creation: Aug 19, 2025 at 03:11 PM
--

CREATE TABLE `carregistration` (
  `reg_no` varchar(120) NOT NULL,
  `brand` varchar(100) NOT NULL,
  `model` varchar(150) NOT NULL,
  `rental` int(11) NOT NULL,
  `Available` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carregistration`
--

INSERT INTO `carregistration` (`reg_no`, `brand`, `model`, `rental`, `Available`) VALUES
('1234', 'SUSZIKI', '102', 500, 'Yes'),
('AUD-2015-01', 'Audi', 'A4 2015 (Manual, Petrol, 5 seats)', 26000, 'Booked'),
('AUD-2016-02', 'Audi', 'A4 2016 (Automatic, Petrol, 5 seats)', 26100, 'Yes'),
('AUD-2017-03', 'Audi', 'A4 2017 (Manual, Petrol, 5 seats)', 26200, 'Booked'),
('AUD-2018-04', 'Audi', 'A4 2018 (Automatic, Petrol, 5 seats)', 26300, 'Yes'),
('AUD-2021-07', 'Audi', 'A4 2021 (Manual, Petrol, 5 seats)', 26600, 'Booked'),
('AUD-2022-08', 'Audi', 'A4 2022 (Automatic, Petrol, 5 seats)', 26700, 'Yes'),
('AUD-2023-09', 'Audi', 'A4 2023 (Manual, Petrol, 5 seats)', 26800, 'Booked'),
('AUD-2024-10', 'Audi', 'A4 2024 (Automatic, Petrol, 5 seats)', 26900, 'Yes'),
('BMW-2015-01', 'BMW', '3 Series 2015 (Manual, Petrol, 5 seats)', 24000, 'Booked'),
('BMW-2016-02', 'BMW', '3 Series 2016 (Automatic, Petrol, 5 seats)', 24100, 'Yes'),
('BMW-2017-03', 'BMW', '3 Series 2017 (Manual, Petrol, 5 seats)', 24200, 'Booked'),
('BMW-2018-04', 'BMW', '3 Series 2018 (Automatic, Petrol, 5 seats)', 24300, 'Yes'),
('BMW-2019-05', 'BMW', '3 Series 2019 (Manual, Petrol, 5 seats)', 24400, 'Booked'),
('BMW-2020-06', 'BMW', '3 Series 2020 (Automatic, Petrol, 5 seats)', 24500, 'Yes'),
('BMW-2021-07', 'BMW', '3 Series 2021 (Manual, Petrol, 5 seats)', 24600, 'Booked'),
('BMW-2022-08', 'BMW', '3 Series 2022 (Automatic, Petrol, 5 seats)', 24700, 'Yes'),
('BMW-2023-09', 'BMW', '3 Series 2023 (Manual, Petrol, 5 seats)', 24800, 'Booked'),
('BMW-2024-10', 'BMW', '3 Series 2024 (Automatic, Petrol, 5 seats)', 24900, 'Yes'),
('FRD-2015-01', 'Ford', 'Focus 2015 (Manual, Petrol, 5 seats)', 8000, 'Booked'),
('FRD-2016-02', 'Ford', 'Focus 2016 (Automatic, Petrol, 5 seats)', 8100, 'Yes'),
('FRD-2017-03', 'Ford', 'Focus 2017 (Manual, Petrol, 5 seats)', 8200, 'Booked'),
('FRD-2018-04', 'Ford', 'Focus 2018 (Automatic, Petrol, 5 seats)', 8300, 'Yes'),
('FRD-2019-05', 'Ford', 'Focus 2019 (Manual, Petrol, 5 seats)', 8400, 'Booked'),
('FRD-2020-06', 'Ford', 'Focus 2020 (Automatic, Petrol, 5 seats)', 8500, 'Yes'),
('FRD-2021-07', 'Ford', 'Focus 2021 (Manual, Petrol, 5 seats)', 8600, 'Booked'),
('FRD-2022-08', 'Ford', 'Focus 2022 (Automatic, Petrol, 5 seats)', 8700, 'Yes'),
('FRD-2023-09', 'Ford', 'Focus 2023 (Manual, Petrol, 5 seats)', 8800, 'Booked'),
('FRD-2024-10', 'Ford', 'Focus 2024 (Automatic, Petrol, 5 seats)', 8900, 'Yes'),
('HND-2015-01', 'Honda', 'Civic 2015 (Manual, Petrol, 5 seats)', 7500, 'Booked'),
('HND-2016-02', 'Honda', 'Civic 2016 (Automatic, Petrol, 5 seats)', 7600, 'Yes'),
('HND-2017-03', 'Honda', 'Civic 2017 (Manual, Petrol, 5 seats)', 7700, 'Booked'),
('HND-2018-04', 'Honda', 'Civic 2018 (Automatic, Petrol, 5 seats)', 7800, 'Yes'),
('HND-2019-05', 'Honda', 'Civic 2019 (Manual, Petrol, 5 seats)', 7900, 'Booked'),
('HND-2020-06', 'Honda', 'Civic 2020 (Automatic, Petrol, 5 seats)', 8000, 'Yes'),
('HND-2021-07', 'Honda', 'Civic 2021 (Manual, Petrol, 5 seats)', 8100, 'Booked'),
('HND-2022-08', 'Honda', 'Civic 2022 (Automatic, Petrol, 5 seats)', 8200, 'Yes'),
('HND-2023-09', 'Honda', 'Civic 2023 (Manual, Petrol, 5 seats)', 8300, 'Booked'),
('HND-2024-10', 'Honda', 'Civic 2024 (Automatic, Petrol, 5 seats)', 8400, 'Yes'),
('HYU-2015-01', 'Hyundai', 'Elantra 2015 (Manual, Petrol, 5 seats)', 7000, 'Booked'),
('HYU-2016-02', 'Hyundai', 'Elantra 2016 (Automatic, Petrol, 5 seats)', 7100, 'Yes'),
('HYU-2017-03', 'Hyundai', 'Elantra 2017 (Manual, Petrol, 5 seats)', 7200, 'Booked'),
('HYU-2018-04', 'Hyundai', 'Elantra 2018 (Automatic, Petrol, 5 seats)', 7300, 'Yes'),
('HYU-2019-05', 'Hyundai', 'Elantra 2019 (Manual, Petrol, 5 seats)', 7400, 'Booked'),
('HYU-2020-06', 'Hyundai', 'Elantra 2020 (Automatic, Petrol, 5 seats)', 7500, 'Yes'),
('HYU-2021-07', 'Hyundai', 'Elantra 2021 (Manual, Petrol, 5 seats)', 7600, 'Booked'),
('HYU-2022-08', 'Hyundai', 'Elantra 2022 (Automatic, Petrol, 5 seats)', 7700, 'Yes'),
('HYU-2023-09', 'Hyundai', 'Elantra 2023 (Manual, Petrol, 5 seats)', 7800, 'Booked'),
('HYU-2024-10', 'Hyundai', 'Elantra 2024 (Automatic, Petrol, 5 seats)', 7900, 'Yes'),
('KIA-2015-01', 'Kia', 'Sportage (SUV) 2015 (Manual, Diesel, 5 seats)', 14000, 'Booked'),
('KIA-2016-02', 'Kia', 'Sportage (SUV) 2016 (Automatic, Diesel, 5 seats)', 14100, 'Yes'),
('KIA-2017-03', 'Kia', 'Sportage (SUV) 2017 (Manual, Diesel, 5 seats)', 14200, 'Booked'),
('KIA-2018-04', 'Kia', 'Sportage (SUV) 2018 (Automatic, Diesel, 5 seats)', 14300, 'Yes'),
('KIA-2019-05', 'Kia', 'Sportage (SUV) 2019 (Manual, Diesel, 5 seats)', 14400, 'Booked'),
('KIA-2020-06', 'Kia', 'Sportage (SUV) 2020 (Automatic, Diesel, 5 seats)', 14500, 'Yes'),
('KIA-2021-07', 'Kia', 'Sportage (SUV) 2021 (Manual, Diesel, 5 seats)', 14600, 'Booked'),
('KIA-2022-08', 'Kia', 'Sportage (SUV) 2022 (Automatic, Diesel, 5 seats)', 14700, 'Yes'),
('KIA-2023-09', 'Kia', 'Sportage (SUV) 2023 (Manual, Diesel, 5 seats)', 14800, 'Booked'),
('KIA-2024-10', 'Kia', 'Sportage (SUV) 2024 (Automatic, Diesel, 5 seats)', 14900, 'Yes'),
('MBZ-2015-01', 'Mercedes-Benz', 'C-Class 2015 (Manual, Petrol, 5 seats)', 30000, 'Booked'),
('MBZ-2016-02', 'Mercedes-Benz', 'C-Class 2016 (Automatic, Petrol, 5 seats)', 30100, 'Yes'),
('MBZ-2017-03', 'Mercedes-Benz', 'C-Class 2017 (Manual, Petrol, 5 seats)', 30200, 'Booked'),
('MBZ-2018-04', 'Mercedes-Benz', 'C-Class 2018 (Automatic, Petrol, 5 seats)', 30300, 'Yes'),
('MBZ-2019-05', 'Mercedes-Benz', 'C-Class 2019 (Manual, Petrol, 5 seats)', 30400, 'Booked'),
('MBZ-2020-06', 'Mercedes-Benz', 'C-Class 2020 (Automatic, Petrol, 5 seats)', 30500, 'Yes'),
('MBZ-2021-07', 'Mercedes-Benz', 'C-Class 2021 (Manual, Petrol, 5 seats)', 30600, 'Booked'),
('MBZ-2022-08', 'Mercedes-Benz', 'C-Class 2022 (Automatic, Petrol, 5 seats)', 30700, 'Yes'),
('MBZ-2023-09', 'Mercedes-Benz', 'C-Class 2023 (Manual, Petrol, 5 seats)', 30800, 'Booked'),
('MBZ-2024-10', 'Mercedes-Benz', 'C-Class 2024 (Automatic, Petrol, 5 seats)', 30900, 'Yes'),
('NIS-2015-01', 'Nissan', 'Leaf 2015 (Manual, Electric, 5 seats)', 12000, 'Booked'),
('NIS-2016-02', 'Nissan', 'Leaf 2016 (Automatic, Electric, 5 seats)', 12100, 'Yes'),
('NIS-2017-03', 'Nissan', 'Leaf 2017 (Manual, Electric, 5 seats)', 12200, 'Booked'),
('NIS-2018-04', 'Nissan', 'Leaf 2018 (Automatic, Electric, 5 seats)', 12300, 'Yes'),
('NIS-2019-05', 'Nissan', 'Leaf 2019 (Manual, Electric, 5 seats)', 12400, 'Booked'),
('NIS-2020-06', 'Nissan', 'Leaf 2020 (Automatic, Electric, 5 seats)', 12500, 'Yes'),
('NIS-2021-07', 'Nissan', 'Leaf 2021 (Manual, Electric, 5 seats)', 12600, 'Booked'),
('NIS-2022-08', 'Nissan', 'Leaf 2022 (Automatic, Electric, 5 seats)', 12700, 'Yes'),
('NIS-2023-09', 'Nissan', 'Leaf 2023 (Manual, Electric, 5 seats)', 12800, 'Booked'),
('NIS-2024-10', 'Nissan', 'Leaf 2024 (Automatic, Electric, 5 seats)', 12900, 'Yes'),
('SUZ-2015-01', 'Suzuki', 'Swift 2015 (Manual, Petrol, 5 seats)', 6000, 'Booked'),
('SUZ-2016-02', 'Suzuki', 'Swift 2016 (Automatic, Petrol, 5 seats)', 6100, 'Yes'),
('SUZ-2018-04', 'Suzuki', 'Swift 2018 (Automatic, Petrol, 5 seats)', 6300, 'Yes'),
('SUZ-2019-05', 'Suzuki', 'Swift 2019 (Manual, Petrol, 5 seats)', 6400, 'Booked'),
('SUZ-2020-06', 'Suzuki', 'Swift 2020 (Automatic, Petrol, 5 seats)', 6500, 'Yes'),
('SUZ-2021-07', 'Suzuki', 'Swift 2021 (Manual, Petrol, 5 seats)', 6600, 'Booked'),
('SUZ-2022-08', 'Suzuki', 'Swift 2022 (Automatic, Petrol, 5 seats)', 6700, 'Yes'),
('SUZ-2023-09', 'Suzuki', 'Swift 2023 (Manual, Petrol, 5 seats)', 6800, 'Booked'),
('SUZ-2024-10', 'Suzuki', 'Swift 2024 (Automatic, Petrol, 5 seats)', 6900, 'Yes'),
('TYT-2015-01', 'Toyota', 'Corolla 2015 (Manual, Petrol, 5 seats)', 7000, 'Booked'),
('TYT-2016-02', 'Toyota', 'Corolla 2016 (Automatic, Petrol, 5 seats)', 7100, 'Yes'),
('TYT-2017-03', 'Toyota', 'Corolla 2017 (Manual, Petrol, 5 seats)', 7200, 'Booked'),
('TYT-2018-04', 'Toyota', 'Corolla 2018 (Automatic, Petrol, 5 seats)', 7300, 'Yes'),
('TYT-2019-05', 'Toyota', 'Corolla 2019 (Manual, Petrol, 5 seats)', 7400, 'Booked'),
('TYT-2020-06', 'Toyota', 'Corolla 2020 (Automatic, Petrol, 5 seats)', 7500, 'Yes'),
('TYT-2021-07', 'Toyota', 'Corolla 2021 (Manual, Petrol, 5 seats)', 7600, 'Booked'),
('TYT-2022-08', 'Toyota', 'Corolla 2022 (Automatic, Petrol, 5 seats)', 7700, 'Yes'),
('TYT-2023-09', 'Toyota', 'Corolla 2023 (Manual, Petrol, 5 seats)', 7800, 'Booked'),
('TYT-2024-10', 'Toyota', 'Corolla 2024 (Automatic, Petrol, 5 seats)', 7900, 'Yes');

-- --------------------------------------------------------

--
-- Table structure for table `carregistration_backup`
--
-- Creation: Aug 17, 2025 at 12:07 PM
--

CREATE TABLE `carregistration_backup` (
  `reg_no` varchar(120) NOT NULL,
  `brand` varchar(120) NOT NULL,
  `model` varchar(120) NOT NULL,
  `rental` varchar(120) NOT NULL,
  `Available` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carregistration_backup`
--

INSERT INTO `carregistration_backup` (`reg_no`, `brand`, `model`, `rental`, `Available`) VALUES
('ABC-1234', 'HONDA', 'PREUS', '7000', 'Yes'),
('CAR-4545', 'HONDA', 'PREUS', '7400', 'Yes'),
('BAC-5561', 'ZUZUKI', 'MARUTI', '5000', 'Yes'),
('ABC-1234', 'HONDA', 'PREUS', '7000', 'Yes'),
('CAR-4545', 'HONDA', 'PREUS', '7400', 'Yes'),
('BAC-5561', 'ZUZUKI', 'MARUTI', '5000', 'Yes'),
('DEF-7788', 'TOYOTA', 'COROLLA', '8500', 'Booked'),
('GHI-8899', 'NISSAN', 'SUNNY', '6200', 'No'),
('JKL-9900', 'FORD', 'FOCUS', '9500', 'Booked'),
('MNO-2211', 'TESLA', 'MODEL 3', '15000', 'Pending'),
('PQR-3344', 'BMW', '320i', '18000', 'Yes'),
('STU-4455', 'AUDI', 'A4', '17500', 'No'),
('VWX-5566', 'KIA', 'SPORTAGE', '9000', 'Booked'),
('LKD-1122', 'TOYOTA', 'YARIS', '6500', 'Yes'),
('MNB-3344', 'HONDA', 'CIVIC', '8000', 'Booked'),
('XYP-5566', 'NISSAN', 'X-TRAIL', '11000', 'No'),
('WTR-7788', 'TESLA', 'MODEL Y', '20000', 'Yes'),
('QAZ-9900', 'BMW', 'X5', '25000', 'Pending'),
('PLM-2211', 'KIA', 'RIO', '7200', 'Yes'),
('HJK-8899', 'SUZUKI', 'SWIFT', '5800', 'Booked'),
('RTY-3344', 'FORD', 'RANGER', '15000', 'No'),
('ABC-1235', 'FZS', '5', '7000', 'Yes'),
('DEF-7788', 'TOYOTA', 'COROLLA', '8500', 'Yes'),
('BCE 1202', 'CIVIC', '6', '5000', 'Yes');

-- --------------------------------------------------------

--
-- Table structure for table `carregistration_src`
--
-- Creation: Aug 13, 2025 at 05:40 AM
--

CREATE TABLE `carregistration_src` (
  `reg_no` varchar(120) NOT NULL,
  `brand` varchar(120) NOT NULL,
  `model` varchar(120) NOT NULL,
  `rental` varchar(120) NOT NULL,
  `Available` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carregistration_src`
--

INSERT INTO `carregistration_src` (`reg_no`, `brand`, `model`, `rental`, `Available`) VALUES
('ABC-1234', 'HONDA', 'PREUS', '7000', 'Yes'),
('CAR-4545', 'HONDA', 'PREUS', '7400', 'Yes'),
('BAC-5561', 'ZUZUKI', 'MARUTI', '5000', 'Yes'),
('ABC-1234', 'HONDA', 'PREUS', '7000', 'Yes'),
('CAR-4545', 'HONDA', 'PREUS', '7400', 'Yes'),
('BAC-5561', 'ZUZUKI', 'MARUTI', '5000', 'Yes'),
('DEF-7788', 'TOYOTA', 'COROLLA', '8500', 'Booked'),
('GHI-8899', 'NISSAN', 'SUNNY', '6200', 'No'),
('JKL-9900', 'FORD', 'FOCUS', '9500', 'Booked'),
('MNO-2211', 'TESLA', 'MODEL 3', '15000', 'Pending'),
('PQR-3344', 'BMW', '320i', '18000', 'Yes'),
('STU-4455', 'AUDI', 'A4', '17500', 'No'),
('VWX-5566', 'KIA', 'SPORTAGE', '9000', 'Booked'),
('LKD-1122', 'TOYOTA', 'YARIS', '6500', 'Yes'),
('MNB-3344', 'HONDA', 'CIVIC', '8000', 'Booked'),
('XYP-5566', 'NISSAN', 'X-TRAIL', '11000', 'No'),
('WTR-7788', 'TESLA', 'MODEL Y', '20000', 'Yes'),
('QAZ-9900', 'BMW', 'X5', '25000', 'Pending'),
('PLM-2211', 'KIA', 'RIO', '7200', 'Yes'),
('HJK-8899', 'SUZUKI', 'SWIFT', '5800', 'Booked'),
('RTY-3344', 'FORD', 'RANGER', '15000', 'No'),
('ABC-1235', 'FZS', '5', '7000', 'Yes'),
('DEF-7788', 'TOYOTA', 'COROLLA', '8500', 'Yes'),
('BCE 1202', 'CIVIC', '6', '5000', 'Yes');

-- --------------------------------------------------------

--
-- Stand-in structure for view `car_availability_view`
-- (See below for the actual view)
--
CREATE TABLE `car_availability_view` (
`reg_no` varchar(120)
,`brand` varchar(60)
,`model_name` varchar(60)
,`year` smallint(6)
,`rental` int(11)
,`car_status` varchar(50)
,`Available` varchar(3)
);

-- --------------------------------------------------------

--
-- Table structure for table `custregestration`
--
-- Creation: Aug 13, 2025 at 05:42 AM
--

CREATE TABLE `custregestration` (
  `NIC` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Address` varchar(255) NOT NULL,
  `Contact` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `custregestration`
--

INSERT INTO `custregestration` (`NIC`, `Name`, `Address`, `Contact`) VALUES
('199856421355', 'SAKUN', 'HETTIPOLA', 76522412),
('2001122154', 'FERNANDO', 'GAMPAHA', 783622097),
('199856421355', 'MALITHI', 'HETTIPOLA', 76522412),
('200102315675', 'KAVINDU PERERA', 'COLOMB', 771234567),
('199845623789', 'DILSHAN MADUSHANKA', 'KURUNEGALA', 772589634),
('200461104251', 'NETHMI NIMSARA', 'BADULLA', 711197884),
('200102315679', 'KAVINDU PERER', 'COLOMBO', 771234567),
('200651300489', 'THISALI VISHAKYA', 'COLOMBO', 710363014),
('200820503080', 'DULNETH NETHMIKA', '120/9 PINNAWALA MADAPATHA PILIYANDALA', 755745676),
('200531205056', 'SADEEPA', 'COLOMBO', 781821958),
('2004114087', 'MINAYA', 'COLOMBO', 779483728),
('200612004810', 'MIUTH', 'COLOMBO', 740707798),
('200414203257', 'CHANIDU', 'CFOLOMBO', 777526919);

-- --------------------------------------------------------

--
-- Table structure for table `model`
--
-- Creation: Aug 19, 2025 at 09:57 AM
--

CREATE TABLE `model` (
  `id` int(11) NOT NULL,
  `brand` varchar(60) NOT NULL,
  `model_name` varchar(60) NOT NULL,
  `year` smallint(6) NOT NULL,
  `seats` tinyint(4) NOT NULL,
  `fuel` varchar(20) NOT NULL,
  `transmission` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `model`
--

INSERT INTO `model` (`id`, `brand`, `model_name`, `year`, `seats`, `fuel`, `transmission`) VALUES
(1, 'Toyota', 'Corolla', 2018, 5, 'Petrol', 'Automatic'),
(2, 'Honda', 'Civic', 2019, 5, 'Petrol', 'Automatic'),
(3, 'Nissan', 'Leaf', 2020, 5, 'Electric', 'Automatic'),
(4, 'Suzuki', 'Wagon R', 2017, 4, 'Petrol', 'Manual'),
(5, 'Hyundai', 'H1 (Van)', 2016, 11, 'Diesel', 'Manual'),
(6, 'Toyota', 'Hiace', 2015, 12, 'Diesel', 'Manual'),
(7, 'BMW', '3 Series', 2021, 5, 'Petrol', 'Automatic'),
(8, 'Toyota', 'Aqua (Hybrid)', 2018, 5, 'Hybrid', 'Automatic'),
(9, 'Kia', 'Sportage (SUV)', 2020, 5, 'Diesel', 'Automatic'),
(10, 'Toyota', 'Land Cruiser Prado', 2017, 7, 'Diesel', 'Automatic'),
(11, 'Mazda', 'Axela', 2016, 5, 'Petrol', 'Automatic'),
(12, 'Nissan', 'NV350 Caravan', 2015, 12, 'Diesel', 'Manual'),
(13, 'Ford', 'Ranger', 2021, 5, 'Diesel', 'Automatic'),
(14, 'Toyota', 'Yaris', 2017, 5, 'Petrol', 'Automatic'),
(15, 'Perodua', 'Axia', 2019, 4, 'Petrol', 'Automatic'),
(16, 'Tesla', 'Model 3', 2022, 5, 'Electric', 'Automatic'),
(17, 'Toyota', 'Premio', 2015, 5, 'Petrol', 'Automatic'),
(18, 'Honda', 'Vezel', 2018, 5, 'Hybrid', 'Automatic'),
(19, 'Suzuki', 'Alto', 2015, 4, 'Petrol', 'Manual'),
(20, 'Mitsubishi', 'Outlander', 2017, 7, 'Petrol', 'Automatic'),
(21, 'Toyota', 'KDH (Hiace Van)', 2014, 12, 'Diesel', 'Manual'),
(22, 'Audi', 'A4', 2020, 5, 'Petrol', 'Automatic'),
(23, 'Toyota', 'Noah', 2016, 7, 'Petrol', 'Automatic'),
(24, 'Mercedes-Benz', 'C-Class', 2019, 5, 'Petrol', 'Automatic'),
(25, 'Suzuki', 'Swift', 2018, 5, 'Petrol', 'Automatic'),
(26, 'Nissan', 'X-Trail', 2017, 5, 'Petrol', 'Automatic'),
(27, 'Honda', 'Fit', 2017, 5, 'Petrol', 'Automatic'),
(28, 'Toyota', 'Vitz', 2016, 5, 'Petrol', 'Automatic'),
(29, 'Toyota', 'Allion', 2015, 5, 'Petrol', 'Automatic'),
(30, 'Kia', 'Picanto', 2019, 4, 'Petrol', 'Automatic'),
(31, 'Volkswagen', 'Polo', 2018, 5, 'Petrol', 'Manual'),
(32, 'Jeep', 'Wrangler', 2024, 5, 'Petrol', 'Manual'),
(33, 'Ford', 'Everest', 2022, 7, 'Diesel', 'Automatic'),
(34, 'Toyota', 'Axio', 2017, 5, 'Petrol', 'Automatic'),
(35, 'Hyundai', 'Grand i10', 2021, 5, 'Petrol', 'Manual'),
(36, 'BYD', 'Atto 3', 2023, 5, 'Electric', 'Automatic'),
(37, 'Toyota', 'Coaster (Bus)', 2014, 30, 'Diesel', 'Manual'),
(38, 'Toyota', 'Venza', 2021, 5, 'Hybrid', 'Automatic'),
(39, 'Toyota', 'Hilux', 2020, 5, 'Diesel', 'Manual'),
(40, 'Honda', 'CR-V', 2019, 5, 'Petrol', 'Automatic'),
(144, 'Toyota', 'Corolla', 2015, 5, 'Petrol', 'Manual'),
(145, 'Toyota', 'Corolla', 2016, 5, 'Petrol', 'Manual'),
(146, 'Toyota', 'Corolla', 2017, 5, 'Petrol', 'Manual'),
(147, 'Toyota', 'Corolla', 2019, 5, 'Petrol', 'Manual'),
(148, 'Toyota', 'Corolla', 2020, 5, 'Petrol', 'Manual'),
(149, 'Toyota', 'Corolla', 2021, 5, 'Petrol', 'Manual'),
(150, 'Toyota', 'Corolla', 2022, 5, 'Petrol', 'Manual'),
(151, 'Toyota', 'Corolla', 2023, 5, 'Petrol', 'Manual'),
(152, 'Toyota', 'Corolla', 2024, 5, 'Petrol', 'Manual'),
(153, 'Honda', 'Civic', 2015, 5, 'Petrol', 'Automatic'),
(154, 'Honda', 'Civic', 2016, 5, 'Petrol', 'Automatic'),
(155, 'Honda', 'Civic', 2017, 5, 'Petrol', 'Automatic'),
(156, 'Honda', 'Civic', 2018, 5, 'Petrol', 'Automatic'),
(157, 'Honda', 'Civic', 2020, 5, 'Petrol', 'Automatic'),
(158, 'Honda', 'Civic', 2021, 5, 'Petrol', 'Automatic'),
(159, 'Honda', 'Civic', 2022, 5, 'Petrol', 'Automatic'),
(160, 'Honda', 'Civic', 2023, 5, 'Petrol', 'Automatic'),
(161, 'Honda', 'Civic', 2024, 5, 'Petrol', 'Automatic'),
(162, 'Nissan', 'Leaf', 2015, 5, 'Electric', 'Manual'),
(163, 'Nissan', 'Leaf', 2016, 5, 'Electric', 'Manual'),
(164, 'Nissan', 'Leaf', 2017, 5, 'Electric', 'Manual'),
(165, 'Nissan', 'Leaf', 2018, 5, 'Electric', 'Manual'),
(166, 'Nissan', 'Leaf', 2019, 5, 'Electric', 'Manual'),
(167, 'Nissan', 'Leaf', 2021, 5, 'Electric', 'Manual'),
(168, 'Nissan', 'Leaf', 2022, 5, 'Electric', 'Manual'),
(169, 'Nissan', 'Leaf', 2023, 5, 'Electric', 'Manual'),
(170, 'Nissan', 'Leaf', 2024, 5, 'Electric', 'Manual'),
(171, 'Suzuki', 'Swift', 2015, 5, 'Petrol', 'Automatic'),
(172, 'Suzuki', 'Swift', 2016, 5, 'Petrol', 'Automatic'),
(173, 'Suzuki', 'Swift', 2017, 5, 'Petrol', 'Automatic'),
(174, 'Suzuki', 'Swift', 2019, 5, 'Petrol', 'Automatic'),
(175, 'Suzuki', 'Swift', 2020, 5, 'Petrol', 'Automatic'),
(176, 'Suzuki', 'Swift', 2021, 5, 'Petrol', 'Automatic'),
(177, 'Suzuki', 'Swift', 2022, 5, 'Petrol', 'Automatic'),
(178, 'Suzuki', 'Swift', 2023, 5, 'Petrol', 'Automatic'),
(179, 'Suzuki', 'Swift', 2024, 5, 'Petrol', 'Automatic'),
(180, 'Hyundai', 'Elantra', 2015, 5, 'Petrol', 'Manual'),
(181, 'Hyundai', 'Elantra', 2016, 5, 'Petrol', 'Manual'),
(182, 'Hyundai', 'Elantra', 2017, 5, 'Petrol', 'Manual'),
(183, 'Hyundai', 'Elantra', 2018, 5, 'Petrol', 'Manual'),
(184, 'Hyundai', 'Elantra', 2019, 5, 'Petrol', 'Manual'),
(185, 'Hyundai', 'Elantra', 2020, 5, 'Petrol', 'Manual'),
(186, 'Hyundai', 'Elantra', 2021, 5, 'Petrol', 'Manual'),
(187, 'Hyundai', 'Elantra', 2022, 5, 'Petrol', 'Manual'),
(188, 'Hyundai', 'Elantra', 2023, 5, 'Petrol', 'Manual'),
(189, 'Hyundai', 'Elantra', 2024, 5, 'Petrol', 'Manual'),
(190, 'Kia', 'Sportage (SUV)', 2015, 5, 'Diesel', 'Automatic'),
(191, 'Kia', 'Sportage (SUV)', 2016, 5, 'Diesel', 'Automatic'),
(192, 'Kia', 'Sportage (SUV)', 2017, 5, 'Diesel', 'Automatic'),
(193, 'Kia', 'Sportage (SUV)', 2018, 5, 'Diesel', 'Automatic'),
(194, 'Kia', 'Sportage (SUV)', 2019, 5, 'Diesel', 'Automatic'),
(195, 'Kia', 'Sportage (SUV)', 2021, 5, 'Diesel', 'Automatic'),
(196, 'Kia', 'Sportage (SUV)', 2022, 5, 'Diesel', 'Automatic'),
(197, 'Kia', 'Sportage (SUV)', 2023, 5, 'Diesel', 'Automatic'),
(198, 'Kia', 'Sportage (SUV)', 2024, 5, 'Diesel', 'Automatic'),
(199, 'Ford', 'Focus', 2015, 5, 'Petrol', 'Manual'),
(200, 'Ford', 'Focus', 2016, 5, 'Petrol', 'Manual'),
(201, 'Ford', 'Focus', 2017, 5, 'Petrol', 'Manual'),
(202, 'Ford', 'Focus', 2018, 5, 'Petrol', 'Manual'),
(203, 'Ford', 'Focus', 2019, 5, 'Petrol', 'Manual'),
(204, 'Ford', 'Focus', 2020, 5, 'Petrol', 'Manual'),
(205, 'Ford', 'Focus', 2021, 5, 'Petrol', 'Manual'),
(206, 'Ford', 'Focus', 2022, 5, 'Petrol', 'Manual'),
(207, 'Ford', 'Focus', 2023, 5, 'Petrol', 'Manual'),
(208, 'Ford', 'Focus', 2024, 5, 'Petrol', 'Manual'),
(209, 'BMW', '3 Series', 2015, 5, 'Petrol', 'Automatic'),
(210, 'BMW', '3 Series', 2016, 5, 'Petrol', 'Automatic'),
(211, 'BMW', '3 Series', 2017, 5, 'Petrol', 'Automatic'),
(212, 'BMW', '3 Series', 2018, 5, 'Petrol', 'Automatic'),
(213, 'BMW', '3 Series', 2019, 5, 'Petrol', 'Automatic'),
(214, 'BMW', '3 Series', 2020, 5, 'Petrol', 'Automatic'),
(215, 'BMW', '3 Series', 2022, 5, 'Petrol', 'Automatic'),
(216, 'BMW', '3 Series', 2023, 5, 'Petrol', 'Automatic'),
(217, 'BMW', '3 Series', 2024, 5, 'Petrol', 'Automatic'),
(218, 'Audi', 'A4', 2015, 5, 'Petrol', 'Manual'),
(219, 'Audi', 'A4', 2016, 5, 'Petrol', 'Manual'),
(220, 'Audi', 'A4', 2017, 5, 'Petrol', 'Manual'),
(221, 'Audi', 'A4', 2018, 5, 'Petrol', 'Manual'),
(222, 'Audi', 'A4', 2019, 5, 'Petrol', 'Manual'),
(223, 'Audi', 'A4', 2021, 5, 'Petrol', 'Manual'),
(224, 'Audi', 'A4', 2022, 5, 'Petrol', 'Manual'),
(225, 'Audi', 'A4', 2023, 5, 'Petrol', 'Manual'),
(226, 'Audi', 'A4', 2024, 5, 'Petrol', 'Manual'),
(227, 'Mercedes-Benz', 'C-Class', 2015, 5, 'Petrol', 'Automatic'),
(228, 'Mercedes-Benz', 'C-Class', 2016, 5, 'Petrol', 'Automatic'),
(229, 'Mercedes-Benz', 'C-Class', 2017, 5, 'Petrol', 'Automatic'),
(230, 'Mercedes-Benz', 'C-Class', 2018, 5, 'Petrol', 'Automatic'),
(231, 'Mercedes-Benz', 'C-Class', 2020, 5, 'Petrol', 'Automatic'),
(232, 'Mercedes-Benz', 'C-Class', 2021, 5, 'Petrol', 'Automatic'),
(233, 'Mercedes-Benz', 'C-Class', 2022, 5, 'Petrol', 'Automatic'),
(234, 'Mercedes-Benz', 'C-Class', 2023, 5, 'Petrol', 'Automatic'),
(235, 'Mercedes-Benz', 'C-Class', 2024, 5, 'Petrol', 'Automatic');

-- --------------------------------------------------------

--
-- Table structure for table `on_rentrecords`
--
-- Creation: Aug 13, 2025 at 05:43 AM
--

CREATE TABLE `on_rentrecords` (
  `id` int(11) NOT NULL,
  `cust_nic` varchar(20) NOT NULL,
  `vehicle_no` varchar(20) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `rent_date` date NOT NULL,
  `return_date` date NOT NULL,
  `total_fee` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `on_rentrecords`
--

INSERT INTO `on_rentrecords` (`id`, `cust_nic`, `vehicle_no`, `brand`, `rent_date`, `return_date`, `total_fee`) VALUES
(1, '200102315678', 'LKD-1122', 'TOYOTA', '2025-08-10', '2025-08-12', 13000),
(7, '200461104251', 'CAR-4545', 'HONDA', '2025-08-15', '2025-08-15', 7400),
(11, '200330911417', 'ABC-1234', 'HONDA', '2025-06-13', '2025-06-14', 7000),
(12, '200102315678', 'CAR-4545', 'HONDA', '2025-08-01', '2025-08-03', 7400),
(13, '200354610614', 'BAC-5561', 'ZUZUKI', '2025-08-20', '2025-08-22', 5000),
(14, '200330911417', 'BMW-2018-04', 'BMW', '2025-06-19', '2025-08-19', 24300),
(15, '200330911417', 'BMW-2018-04', 'BMW', '2025-06-19', '2025-08-19', 24300),
(16, '200340911417', 'BMW-2018-04', 'ZUZUKI', '2025-06-14', '2025-09-15', 5000),
(17, '20033011407', 'AUD-2024-10', 'Audi', '2025-08-20', '2025-08-20', 26900),
(18, '', 'AUD-2020-06', 'Audi', '2025-08-20', '2025-12-19', 26500),
(19, '11111111111', 'AUD-2024-10', 'Audi', '2025-08-20', '2025-10-20', 26900),
(20, '01215400452150', 'AUD-2022-08', 'Audi', '2025-08-20', '2025-09-20', 26700),
(21, '200330911417', 'FRD-2020-06', 'Ford', '2025-08-21', '2025-08-21', 8500),
(22, '200425557562', 'BMW-2016-02', 'BMW', '2025-08-21', '2025-08-21', 24100),
(23, '200425557562', 'BMW-2016-02', 'BMW', '2025-08-21', '2025-08-21', 24100);

-- --------------------------------------------------------

--
-- Table structure for table `rental_records`
--
-- Creation: Aug 13, 2025 at 05:41 AM
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
(12, '200104301676', 'BAC-5561', 'ZUZUKI', '2025-06-14', '2025-06-15', '5000'),
(14, '199856421355', 'CAR-4545', 'HONDA', '2025-06-14', '2025-06-16', '14800'),
(15, '200134512378', 'WTR-7788', 'TESLA', '2025-07-25', '2025-07-27', '40000'),
(16, '199756412398', 'QAZ-9900', 'BMW', '2025-07-28', '2025-07-31', '75000'),
(17, '200102315678', 'PLM-2211', 'KIA', '2025-08-01', '2025-08-03', '14400'),
(18, '199923415674', 'HJK-8899', 'SUZUKI', '2025-08-02', '2025-08-04', '11600'),
(19, '199856421355', 'BAC-5561', 'ZUZUKI', '2025-08-15', '2025-09-15', '5000'),
(22, '200461104251', 'CAR-4545', 'HONDA', '2025-08-15', '2025-08-15', '7400'),
(23, '200102315678', 'ABC-1234', 'HONDA', '2025-09-15', '2025-08-15', '7000'),
(24, '200311417', 'ABC-1234', 'HONDA', '2025-08-18', '2025-08-18', '7000'),
(25, '200330911417', 'ABC-1234', 'HONDA', '2025-08-15', '2025-08-15', '7000'),
(26, '200330911417', 'ABC-1234', 'HONDA', '2025-06-13', '2025-06-14', '7000'),
(27, '200102315678', 'CAR-4545', 'HONDA', '2025-08-01', '2025-08-03', '7400'),
(28, '200354610614', 'BAC-5561', 'ZUZUKI', '2025-08-20', '2025-08-22', '5000'),
(29, '200330911417', 'BMW-2018-04', 'BMW', '2025-06-19', '2025-08-19', '24300'),
(30, '200330911417', 'BMW-2018-04', 'BMW', '2025-06-19', '2025-08-19', '24300'),
(31, '200340911417', 'BMW-2018-04', 'ZUZUKI', '2025-06-14', '2025-09-15', '5000'),
(32, '20033011407', 'AUD-2024-10', 'Audi', '2025-08-20', '2025-08-20', '26900'),
(33, '', 'AUD-2020-06', 'Audi', '2025-08-20', '2025-12-19', '26500'),
(34, '11111111111', 'AUD-2024-10', 'Audi', '2025-08-20', '2025-10-20', '26900'),
(35, '01215400452150', 'AUD-2022-08', 'Audi', '2025-08-20', '2025-09-20', '26700'),
(36, '200330911417', 'FRD-2020-06', 'Ford', '2025-08-21', '2025-08-21', '8500'),
(37, '200425557562', 'BMW-2016-02', 'BMW', '2025-08-21', '2025-08-21', '24100'),
(38, '200425557562', 'BMW-2016-02', 'BMW', '2025-08-21', '2025-08-21', '24100');

-- --------------------------------------------------------

--
-- Table structure for table `status_lu`
--
-- Creation: Aug 19, 2025 at 09:57 AM
--

CREATE TABLE `status_lu` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `status_lu`
--

INSERT INTO `status_lu` (`id`, `name`) VALUES
(1, 'Available'),
(5, 'Booked'),
(2, 'Rented'),
(4, 'Reserved'),
(3, 'Under Maintenance');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--
-- Creation: Aug 13, 2025 at 05:42 AM
--

CREATE TABLE `user` (
  `ID` int(11) NOT NULL,
  `Password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`ID`, `Password`) VALUES
(1, '1234'),
(2, '20030215'),
(3, '20040420'),
(4, 'KAVISHKA'),
(5, '1234'),
(10202, '1234'),
(10203, '1234'),
(10205, '1234'),
(102066, '12345'),
(102067, 'admin2025'),
(102068, 'pass123'),
(102069, 'secure01'),
(102070, 'qwerty');

-- --------------------------------------------------------

--
-- Structure for view `booked_now` exported as a table
--
DROP TABLE IF EXISTS `booked_now`;
CREATE TABLE`booked_now`(
    `id` int(11) NOT NULL DEFAULT '0',
    `reg_no` varchar(120) COLLATE utf8mb4_general_ci NOT NULL,
    `customer_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
    `start_date` date NOT NULL,
    `end_date` date NOT NULL,
    `status` enum('Booked','Collected','Completed','Cancelled') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Booked'
);

-- --------------------------------------------------------

--
-- Structure for view `car_availability_view` exported as a table
--
DROP TABLE IF EXISTS `car_availability_view`;
CREATE TABLE`car_availability_view`(
    `reg_no` varchar(120) COLLATE utf8mb4_general_ci NOT NULL,
    `brand` varchar(60) COLLATE utf8mb4_general_ci NOT NULL,
    `model_name` varchar(60) COLLATE utf8mb4_general_ci NOT NULL,
    `year` smallint(6) NOT NULL,
    `rental` int(11) NOT NULL,
    `car_status` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `Available` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL
);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_bookings_dates` (`start_date`,`end_date`),
  ADD KEY `idx_bookings_reg` (`reg_no`);

--
-- Indexes for table `brand`
--
ALTER TABLE `brand`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_brand_name` (`name`);

--
-- Indexes for table `car`
--
ALTER TABLE `car`
  ADD PRIMARY KEY (`reg_no`),
  ADD KEY `idx_car_model` (`model_id`),
  ADD KEY `idx_car_status` (`status_id`);

--
-- Indexes for table `carregistration`
--
ALTER TABLE `carregistration`
  ADD PRIMARY KEY (`reg_no`);

--
-- Indexes for table `model`
--
ALTER TABLE `model`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `u_model` (`brand`,`model_name`,`year`);

--
-- Indexes for table `on_rentrecords`
--
ALTER TABLE `on_rentrecords`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rental_records`
--
ALTER TABLE `rental_records`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `status_lu`
--
ALTER TABLE `status_lu`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `brand`
--
ALTER TABLE `brand`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `model`
--
ALTER TABLE `model`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=272;

--
-- AUTO_INCREMENT for table `on_rentrecords`
--
ALTER TABLE `on_rentrecords`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `rental_records`
--
ALTER TABLE `rental_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `status_lu`
--
ALTER TABLE `status_lu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=102071;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `fk_bookings_car` FOREIGN KEY (`reg_no`) REFERENCES `car` (`reg_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `car`
--
ALTER TABLE `car`
  ADD CONSTRAINT `fk_car_model` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_car_status` FOREIGN KEY (`status_id`) REFERENCES `status_lu` (`id`) ON UPDATE CASCADE;


--
-- Metadata
--
USE `phpmyadmin`;

--
-- Metadata for table booked_now
--

--
-- Metadata for table bookings
--

--
-- Metadata for table brand
--

--
-- Metadata for table car
--

--
-- Metadata for table carregistration
--

--
-- Metadata for table carregistration_backup
--

--
-- Metadata for table carregistration_src
--

--
-- Metadata for table car_availability_view
--

--
-- Metadata for table custregestration
--

--
-- Metadata for table model
--

--
-- Metadata for table on_rentrecords
--

--
-- Metadata for table rental_records
--

--
-- Metadata for table status_lu
--

--
-- Metadata for table user
--

--
-- Metadata for database car_rental
--
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
