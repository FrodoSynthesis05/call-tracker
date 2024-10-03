-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 10, 2023 at 03:39 AM
-- Server version: 5.7.36
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `work`
--
CREATE DATABASE IF NOT EXISTS `work` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `work`;

-- --------------------------------------------------------

--
-- Table structure for table `calls`
--

CREATE TABLE `calls` (
  `id` varchar(10) NOT NULL,
  `length` int(255) DEFAULT NULL,
  `dollars` varchar(255) DEFAULT NULL,
  `pesos` varchar(255) DEFAULT NULL,
  `timestamp` timestamp(6) NULL DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `calls`
--
ALTER TABLE `calls`
  ADD PRIMARY KEY (`id`);
COMMIT;
