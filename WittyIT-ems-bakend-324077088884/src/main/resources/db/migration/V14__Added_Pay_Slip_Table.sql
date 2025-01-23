

DROP TABLE IF EXISTS `pay_slip`;

CREATE TABLE `pay_slip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_id` int(11) NOT NULL,
  `working_days` float NOT NULL,
  `basic_salary` float NOT NULL,
  `hra` float DEFAULT NULL,
  `medical` float DEFAULT NULL,
  `special_allowances` float DEFAULT NULL,
  `lta` float DEFAULT NULL,
  `vehicle_running` float DEFAULT NULL,
  `attire_allowances` float DEFAULT NULL,
  `vehicle_reimburse` float DEFAULT NULL,
  `telephone_reimburse` float DEFAULT NULL,
  `employer_pf` float DEFAULT NULL,
  `employer_esi` float DEFAULT NULL,
  `employee_pf` float DEFAULT NULL,
  `employee_esi` float DEFAULT NULL,
  `deduction_tds` float DEFAULT NULL,
  `deduction_leave` float DEFAULT NULL,
  `absent` float DEFAULT NULL,
  `other_deductions` float DEFAULT NULL,
  `month` int(3) NOT NULL,
  `year` int(5) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;