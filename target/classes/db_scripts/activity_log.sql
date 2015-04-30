CREATE TABLE `cardreader`.`activity_log` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `access` varchar(45) NOT NULL,
  `emergencybutton` varchar(45) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

