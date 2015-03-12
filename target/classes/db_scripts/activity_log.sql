CREATE TABLE `activity_log` (
  `username` varchar(45) NOT NULL,
  `access` varchar(45) NOT NULL,
  `emergencybutton` varchar(45) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
