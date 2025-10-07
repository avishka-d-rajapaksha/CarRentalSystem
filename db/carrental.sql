-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 07, 2025 at 04:27 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `carrental`
--

-- --------------------------------------------------------

--
-- Table structure for table `audit_log`
--

CREATE TABLE `audit_log` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `action` varchar(50) NOT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `details` text DEFAULT NULL,
  `created_at` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `pickup_ts` bigint(20) NOT NULL,
  `return_ts` bigint(20) NOT NULL,
  `status` enum('RESERVED','RENTED','CANCELLED','COMPLETED','NO_SHOW') NOT NULL,
  `rate_cents` int(11) NOT NULL,
  `deposit_cents` int(11) NOT NULL DEFAULT 0,
  `tax_rate_bp` int(11) NOT NULL DEFAULT 0,
  `discount_cents` int(11) NOT NULL DEFAULT 0,
  `pickup_odometer` int(11) DEFAULT NULL,
  `return_odometer` int(11) DEFAULT NULL,
  `late_fee_cents` int(11) NOT NULL DEFAULT 0,
  `notes` text DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`id`, `vehicle_id`, `customer_id`, `pickup_ts`, `return_ts`, `status`, `rate_cents`, `deposit_cents`, `tax_rate_bp`, `discount_cents`, `pickup_odometer`, `return_odometer`, `late_fee_cents`, `notes`, `created_by`, `created_at`, `updated_at`) VALUES
(31, 32, 1, 1756701000, 1761762600, 'COMPLETED', 6000, 2000, 0, 0, NULL, 0, 330000, NULL, 2, 1759078565, 1759737720),
(32, 33, 2, 1754796600, 1755228600, 'COMPLETED', 5500, 1500, 0, 0, NULL, NULL, 0, NULL, 3, 1759078565, NULL),
(33, 31, 3, 1752978600, 1759689000, 'COMPLETED', 5000, 0, 0, 0, NULL, 0, 365000, NULL, 2, 1759078565, 1759725248),
(34, 34, 4, 1757485800, 1757658600, 'RENTED', 4500, 1000, 0, 0, NULL, NULL, 0, NULL, 3, 1759078565, NULL),
(35, 1, 1, 1759708800, 1761782400, 'RESERVED', 120000, 60000, 0, 0, NULL, NULL, 0, '', 1, 1759716882, NULL),
(36, 1, 3, 1759968000, 1761696000, 'RESERVED', 100000, 5000, 0, 0, NULL, NULL, 0, '', 1, 1759840905, NULL),
(37, 1, 1, 1759276800, 1761782400, 'RESERVED', 145000, 5000, 0, 0, NULL, NULL, 0, '', 1, 1759843951, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
  `full_name` varchar(150) NOT NULL,
  `license_no` varchar(50) NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `blacklisted` tinyint(1) NOT NULL DEFAULT 0,
  `notes` text DEFAULT NULL,
  `created_at` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`id`, `full_name`, `license_no`, `phone`, `email`, `address`, `blacklisted`, `notes`, `created_at`) VALUES
(1, 'Nimal Perera', 'B1234567', '0771234561', 'nimal.p@mail.com', 'Colombo', 0, NULL, 1758851001),
(2, 'Kumara Silva', 'B1234568', '0771234562', 'kumara.s@mail.com', 'Gampaha', 0, NULL, 1758851002),
(3, 'Chathura Fernando', 'B1234569', '0771234563', 'chathura.f@mail.com', 'Kandy', 0, NULL, 1758851003),
(4, 'Sanduni Jayasinghe', 'B1234570', '0771234564', 'sanduni.j@mail.com', 'Matara', 0, NULL, 1758851004),
(5, 'Heshan Perera', 'B1234571', '0771234565', 'heshan.p@mail.com', 'Galle', 0, NULL, 1758851005),
(6, 'Kasun Wickramasinghe', 'B1234572', '0771234566', 'kasun.w@mail.com', 'Kurunegala', 0, NULL, 1758851006),
(7, 'Ravindu Silva', 'B1234573', '0771234567', 'ravindu.s@mail.com', 'Jaffna', 0, NULL, 1758851007),
(9, 'Roshan Gunawardena', 'B1234575', '0771234569', 'roshan.g@mail.com', 'Polonnaruwa', 0, NULL, 1758851009),
(10, 'Manjula Abeywardena', 'B1234576', '0771234570', 'manjula.a@mail.com', 'Kalutara', 0, NULL, 1758851010),
(11, 'Tharindu Rajapaksha', 'B1234577', '0771234571', 'tharindu.r@mail.com', 'Ratnapura', 1, '', 1758851011),
(12, 'Priyanka Jayawardena', 'B1234578', '0771234572', 'priyanka.j@mail.com', 'Puttalam', 0, NULL, 1758851012),
(13, 'Niluka Wijesinghe', 'B1234579', '0771234573', 'niluka.w@mail.com', 'Badulla', 1, '', 1758851013),
(14, 'Chamara Fernando', 'B1234580', '0771234574', 'chamara.f@mail.com', 'Matale', 0, NULL, 1758851014),
(15, 'Nisansala Perera', 'B1234581', '0771234575', 'nisansala.p@mail.com', 'Hambantota', 0, NULL, 1758851015),
(16, 'Ruwan Silva', 'B1234582', '0771234576', 'ruwan.s@mail.com', 'Monaragala', 0, NULL, 1758851016),
(17, 'Hiruni Jayawardena', 'B1234583', '0771234577', 'hiruni.j@mail.com', 'Trincomalee', 0, 'd', 1758851017),
(18, 'Prasad Senanayake', 'B1234584', '0771234578', 'prasad.s@mail.com', 'Batticaloa', 0, NULL, 1758851018),
(19, 'Nadeesha Wickramasinghe', 'B1234585', '0771234579', 'nadeesha.w@mail.com', 'Vavuniya', 0, NULL, 1758851019),
(21, 'Uma Perera', 'B1234587', '0771234581', 'uma.p@mail.com', 'Colombo', 0, NULL, 1758851021),
(22, 'Victor Fernando', 'B1234588', '0771234582', 'victor.f@mail.com', 'Gampaha', 0, NULL, 1758851022),
(23, 'Wendy Jayawardena', 'B1234589', '0771234583', 'wendy.j@mail.com', 'Kandy', 0, NULL, 1758851023),
(24, 'Xander Silva', 'B1234590', '0771234584', 'xander.s@mail.com', 'Kalutara', 0, NULL, 1758851024),
(25, 'Yara Perera', 'B1234591', '0771234585', 'yara.p@mail.com', 'Ratnapura', 0, NULL, 1758851025),
(26, 'Zane Fernando', 'B1234592', '0771234586', 'zane.f@mail.com', 'Puttalam', 0, NULL, 1758851026),
(27, 'Amy Jayasuriya', 'B1234593', '0771234587', 'amy.j@mail.com', 'Badulla', 0, NULL, 1758851027),
(28, 'Brian Silva', 'B1234594', '0771234588', 'brian.s@mail.com', 'Anuradhapura', 0, NULL, 1758851028),
(29, 'Cathy De Silva', 'B1234595', '0771234589', 'cathy.d@mail.com', 'Polonnaruwa', 0, NULL, 1758851029),
(30, 'Derek Perera', 'B1234596', '0771234590', 'derek.p@mail.com', 'Galle', 0, NULL, 1758851030);

-- --------------------------------------------------------

--
-- Table structure for table `invoices`
--

CREATE TABLE `invoices` (
  `id` int(11) NOT NULL,
  `booking_id` int(11) NOT NULL,
  `invoice_no` varchar(50) NOT NULL,
  `issued_at` bigint(20) NOT NULL,
  `subtotal_cents` int(11) NOT NULL,
  `tax_cents` int(11) NOT NULL,
  `total_cents` int(11) NOT NULL,
  `balance_cents` int(11) NOT NULL,
  `pdf_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_items`
--

CREATE TABLE `invoice_items` (
  `id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `qty` int(11) NOT NULL DEFAULT 1,
  `unit_cents` int(11) NOT NULL,
  `total_cents` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `booking_id` int(11) NOT NULL,
  `category` enum('DEPOSIT','RENTAL','LATE_FEE','OTHER') NOT NULL,
  `method` enum('CASH','CARD','DIGITAL') NOT NULL,
  `amount_cents` int(11) NOT NULL,
  `paid_at` bigint(20) NOT NULL,
  `reference` varchar(100) DEFAULT NULL,
  `status` enum('CAPTURED','VOIDED','REFUNDED') NOT NULL DEFAULT 'CAPTURED',
  `created_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `booking_id`, `category`, `method`, `amount_cents`, `paid_at`, `reference`, `status`, `created_by`) VALUES
(31, 31, 'RENTAL', 'CASH', 600000, 1756704600, NULL, 'CAPTURED', 2),
(32, 32, 'DEPOSIT', 'CARD', 150000, 1754800200, NULL, 'CAPTURED', 3),
(33, 33, 'OTHER', 'DIGITAL', 50000, 1752982200, NULL, 'CAPTURED', 2),
(34, 34, 'LATE_FEE', 'CASH', 20000, 1757489400, NULL, 'CAPTURED', 3);

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `key` varchar(100) NOT NULL,
  `value` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`key`, `value`) VALUES
('company.name', 'My Car Rentals'),
('tax.rate_bp', '1300'),
('theme', 'light');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(150) DEFAULT NULL,
  `role` enum('ADMIN','MANAGER','STAFF','USER') DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` bigint(20) NOT NULL,
  `last_login_at` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password_hash`, `full_name`, `role`, `is_active`, `created_at`, `last_login_at`) VALUES
(1, 'admin', '12345', 'Administrator', 'ADMIN', 1, 1758859697, NULL),
(2, 'admin1', 'pass1', 'Avishka Rajapaksha', 'ADMIN', 1, 1758850001, NULL),
(3, 'manager1', 'pass2', 'Nimal Perera', 'MANAGER', 1, 1758850002, NULL),
(4, 'staff1', 'pass3', 'Chamara Fernando', 'STAFF', 1, 1758850003, NULL),
(5, 'staff2', 'pass4', 'Dilani Jayasinghe', 'STAFF', 1, 1758850004, NULL),
(6, 'staff3', 'pass5', 'Samanthi Silva', 'STAFF', 1, 1758850005, NULL),
(7, 'staff4', 'pass6', 'Kasun Wickramasinghe', 'STAFF', 1, 1758850006, NULL),
(8, 'staff5', 'pass7', 'Hiruni Jayawardena', 'STAFF', 1, 1758850007, NULL),
(9, 'staff6', 'pass8', 'Prasad Senanayake', 'STAFF', 1, 1758850008, NULL),
(10, 'staff7', 'pass9', 'Nadeesha Amarasinghe', 'STAFF', 1, 1758850009, NULL),
(11, 'staff8', 'pass10', 'Ruwan Wijesinghe', 'STAFF', 1, 1758850010, NULL),
(12, 'staff9', 'pass11', 'Tharindu Gunawardena', 'STAFF', 1, 1758850011, NULL),
(13, 'staff10', 'pass12', 'Sanduni Perera', 'STAFF', 1, 1758850012, NULL),
(14, 'staff11', 'pass13', 'Kavinda Jayasuriya', 'STAFF', 1, 1758850013, NULL),
(15, 'staff12', 'pass14', 'Roshan Fernando', 'STAFF', 1, 1758850014, NULL),
(16, 'staff13', 'pass15', 'Nisansala Wickramaratne', 'STAFF', 1, 1758850015, NULL),
(17, 'staff14', 'pass16', 'Ravindu De Silva', 'STAFF', 1, 1758850016, NULL),
(18, 'staff15', 'pass17', 'Manjula Hettiarachchi', 'STAFF', 1, 1758850017, NULL),
(19, 'staff16', 'pass18', 'Heshan Perera', 'STAFF', 1, 1758850018, NULL),
(20, 'staff17', 'pass19', 'Niluka Abeywardena', 'STAFF', 1, 1758850019, NULL),
(21, 'staff18', 'pass20', 'Dulani Rathnayake', 'STAFF', 1, 1758850020, NULL),
(22, 'staff19', 'pass21', 'Chathura Gunasekara', 'STAFF', 1, 1758850021, NULL),
(23, 'staff20', 'pass22', 'Priyanka Silva', 'STAFF', 1, 1758850022, NULL),
(24, 'staff21', 'pass23', 'Ravindu Fernando', 'STAFF', 1, 1758850023, NULL),
(25, 'staff22', 'pass24', 'Sandun Perera', 'STAFF', 1, 1758850024, NULL),
(26, 'staff23', 'pass25', 'Harini Jayawardena', 'STAFF', 1, 1758850025, NULL),
(27, 'staff24', 'pass26', 'Thilina Wickramasinghe', 'STAFF', 1, 1758850026, NULL),
(28, 'staff25', 'pass27', 'Chamudi Silva', 'STAFF', 1, 1758850027, NULL),
(29, 'staff26', 'pass28', 'Roshan Perera', 'STAFF', 1, 1758850028, NULL),
(30, 'staff27', 'pass29', 'Nethmi De Silva', 'STAFF', 1, 1758850029, NULL),
(31, 'staff28', 'pass30', 'Amila Jayasundara', 'STAFF', 1, 1758850030, NULL),
(33, 'ss', '1', 's', 'USER', 1, 1759074269, NULL),
(34, 'a', '1', 'aaa', 'USER', 1, 1759074304, NULL),
(35, 'aa', 'aa', 'aa', 'USER', 1, 1759077125, NULL),
(36, 'b', 'b', 'b', 'USER', 1, 1759077903, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `vehicles`
--

CREATE TABLE `vehicles` (
  `id` int(11) NOT NULL,
  `plate_no` varchar(20) NOT NULL,
  `vin` varchar(32) DEFAULT NULL,
  `make` varchar(80) NOT NULL,
  `model` varchar(80) NOT NULL,
  `year` int(11) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `odometer` int(11) DEFAULT NULL,
  `daily_rate_cents` int(11) NOT NULL DEFAULT 0,
  `status` enum('AVAILABLE','RENTED','MAINTENANCE','RETIRED') NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicles`
--

INSERT INTO `vehicles` (`id`, `plate_no`, `vin`, `make`, `model`, `year`, `color`, `odometer`, `daily_rate_cents`, `status`, `image_path`, `notes`, `created_at`, `updated_at`) VALUES
(1, 'WP GA 1234', 'VIN001', 'Toyota', 'Corolla', 2020, 'White', 15000, 5000, 'AVAILABLE', NULL, NULL, 1758852001, NULL),
(2, 'WP GA 1235', 'VIN002', 'Honda', 'Civic', 2019, 'Black', 25000, 6000, 'AVAILABLE', NULL, NULL, 1758852002, NULL),
(5, 'WP GA 1238', 'VIN005', 'Mitsubishi', 'Lancer', 2020, 'Grey', 12000, 5000, 'AVAILABLE', NULL, NULL, 1758852005, NULL),
(6, 'WP GA 1239', 'VIN006', 'Toyota', 'Axio', 2022, 'White', 8000, 7000, 'AVAILABLE', NULL, NULL, 1758852006, NULL),
(7, 'WP GA 1240', 'VIN007', 'Honda', 'Fit', 2021, 'Black', 9000, 6500, 'AVAILABLE', NULL, NULL, 1758852007, NULL),
(8, 'WP GA 1241', 'VIN008', 'Nissan', 'Sunny', 2022, 'Silver', 7000, 7200, 'AVAILABLE', NULL, NULL, 1758852008, NULL),
(10, 'WP GA 1243', 'VIN010', 'Mitsubishi', 'Outlander', 2020, 'Red', 20000, 8000, 'AVAILABLE', NULL, NULL, 1758852010, NULL),
(11, 'WP GA 1244', 'VIN011', 'Toyota', 'RAV4', 2019, 'White', 25000, 9000, 'RENTED', NULL, NULL, 1758852011, NULL),
(12, 'WP GA 1245', 'VIN012', 'Honda', 'HR-V', 2021, 'Black', 12000, 8500, 'AVAILABLE', NULL, NULL, 1758852012, NULL),
(13, 'WP GA 1246', 'VIN013', 'Nissan', 'X-Trail', 2022, 'Blue', 8000, 9200, 'AVAILABLE', NULL, NULL, 1758852013, NULL),
(14, 'WP GA 1247', 'VIN014', 'Suzuki', 'Ciaz', 2020, 'Grey', 15000, 5500, 'AVAILABLE', NULL, NULL, 1758852014, NULL),
(15, 'WP GA 1248', 'VIN015', 'Mitsubishi', 'Pajero', 2021, 'White', 10000, 10000, 'AVAILABLE', NULL, NULL, 1758852015, NULL),
(16, 'WP GA 1249', 'VIN016', 'Toyota', 'Hilux', 2022, 'Black', 5000, 12000, 'AVAILABLE', NULL, NULL, 1758852016, NULL),
(17, 'WP GA 1250', 'VIN017', 'Honda', 'City', 2021, 'Silver', 8000, 7500, 'AVAILABLE', NULL, NULL, 1758852017, NULL),
(18, 'WP GA 1251', 'VIN018', 'Nissan', 'Navara', 2022, 'White', 7000, 11500, 'AVAILABLE', NULL, NULL, 1758852018, NULL),
(19, 'WP GA 1252', 'VIN019', 'Suzuki', 'Ertiga', 2020, 'Red', 12000, 5000, 'AVAILABLE', NULL, NULL, 1758852019, NULL),
(20, 'WP GA 1253', 'VIN020', 'Mitsubishi', 'Montero', 2021, 'Blue', 15000, 6500, 'AVAILABLE', NULL, NULL, 1758852020, NULL),
(21, 'WP GA 1254', 'VIN021', 'Toyota', 'Camry', 2021, 'White', 9000, 7000, 'AVAILABLE', NULL, NULL, 1758852021, NULL),
(22, 'WP GA 1255', 'VIN022', 'Honda', 'Accord', 2022, 'Black', 6000, 7500, 'AVAILABLE', NULL, NULL, 1758852022, NULL),
(23, 'WP GA 1256', 'VIN023', 'Nissan', 'Altima', 2020, 'Grey', 20000, 5000, 'AVAILABLE', NULL, NULL, 1758852023, NULL),
(24, 'WP GA 1257', 'VIN024', 'Suzuki', 'Celerio', 2021, 'Blue', 10000, 5500, 'AVAILABLE', NULL, NULL, 1758852024, NULL),
(25, 'WP GA 1258', 'VIN025', 'Mitsubishi', 'L200', 2022, 'White', 8000, 6000, 'AVAILABLE', NULL, NULL, 1758852025, NULL),
(26, 'WP GA 1259', 'VIN026', 'Toyota', 'Prius', 2020, 'Black', 15000, 11000, 'AVAILABLE', NULL, NULL, 1758852026, NULL),
(27, 'WP GA 1260', 'VIN027', 'Honda', 'Vezel', 2021, 'Silver', 7000, 9500, 'AVAILABLE', NULL, NULL, 1758852027, NULL),
(28, 'WP GA 1261', 'VIN028', 'Nissan', 'Juke', 2022, 'White', 6000, 10500, 'AVAILABLE', NULL, NULL, 1758852028, NULL),
(29, 'WP GA 1262', 'VIN029', 'Suzuki', 'Vitara', 2021, 'Blue', 9000, 5000, 'AVAILABLE', NULL, NULL, 1758852029, NULL),
(30, 'WP GA 1263', 'VIN030', 'Mitsubishi', 'Eclipse', 2020, 'Red', 12000, 5500, 'AVAILABLE', NULL, NULL, 1758852030, NULL),
(31, 'WP GA 1264', NULL, 'Toyota', 'Corolla', 2021, 'White', 15000, 5000, 'AVAILABLE', NULL, NULL, 1759078565, 1759725248),
(32, 'WP GA 1265', NULL, 'Honda', 'Civic', 2022, 'Black', 12000, 6000, 'AVAILABLE', NULL, NULL, 1759078565, 1759737720),
(33, 'WP GA 1266', NULL, 'Nissan', 'Altima', 2020, 'Blue', 20000, 5500, 'AVAILABLE', NULL, NULL, 1759078565, NULL),
(34, 'WP GA 1267', NULL, 'Suzuki', 'Swift', 2019, 'Red', 25000, 4500, 'MAINTENANCE', NULL, NULL, 1759078565, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `vehicle_maintenance`
--

CREATE TABLE `vehicle_maintenance` (
  `id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `service_date` bigint(20) NOT NULL,
  `description` text NOT NULL,
  `cost_cents` int(11) NOT NULL DEFAULT 0,
  `next_due_date` bigint(20) DEFAULT NULL,
  `odometer` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `audit_log`
--
ALTER TABLE `audit_log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_audit_user` (`user_id`);

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_booking_customer` (`customer_id`),
  ADD KEY `fk_booking_user` (`created_by`),
  ADD KEY `idx_bookings_vehicle_time` (`vehicle_id`,`pickup_ts`,`return_ts`),
  ADD KEY `idx_bookings_status` (`status`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `license_no` (`license_no`);

--
-- Indexes for table `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `booking_id` (`booking_id`),
  ADD UNIQUE KEY `invoice_no` (`invoice_no`);

--
-- Indexes for table `invoice_items`
--
ALTER TABLE `invoice_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_item_invoice` (`invoice_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_payment_booking` (`booking_id`),
  ADD KEY `fk_payment_user` (`created_by`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`key`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `plate_no` (`plate_no`);

--
-- Indexes for table `vehicle_maintenance`
--
ALTER TABLE `vehicle_maintenance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_vm_vehicle` (`vehicle_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `audit_log`
--
ALTER TABLE `audit_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `invoices`
--
ALTER TABLE `invoices`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice_items`
--
ALTER TABLE `invoice_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `vehicles`
--
ALTER TABLE `vehicles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `vehicle_maintenance`
--
ALTER TABLE `vehicle_maintenance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `audit_log`
--
ALTER TABLE `audit_log`
  ADD CONSTRAINT `fk_audit_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `fk_booking_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  ADD CONSTRAINT `fk_booking_user` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_booking_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`);

--
-- Constraints for table `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `fk_invoice_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `invoice_items`
--
ALTER TABLE `invoice_items`
  ADD CONSTRAINT `fk_item_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `fk_payment_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_payment_user` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `vehicle_maintenance`
--
ALTER TABLE `vehicle_maintenance`
  ADD CONSTRAINT `fk_vm_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
