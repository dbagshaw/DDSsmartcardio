CREATE TABLE `employees` (
  `username` varchar(45) NOT NULL COMMENT 'name of the card owner',
  `cardnumber` varchar(70) NOT NULL COMMENT 'unique identifier of the card',
  `creationdate` datetime DEFAULT NULL COMMENT 'date entry was created',
  `modificationdate` datetime DEFAULT NULL COMMENT 'Date entry was last modified',
  `clinicalaccess` varchar(1) NOT NULL COMMENT 'indicates if the user has access to the system. if "n" the user can only access the system via the break the glass button.',
  PRIMARY KEY (`cardnumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
