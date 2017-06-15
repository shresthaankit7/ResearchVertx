
CREATE DATABASE storedProc;
use storedProc;

USE `storedProc`;
DROP procedure IF EXISTS `Proc_1`;

DELIMITER $$
USE `storedProc`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `Proc_1`()
BEGIN
	DO SLEEP(ROUND((RAND() * (10-5))+5));
	SELECT 1 as `output`;
END$$

DELIMITER ;


USE `storedProc`;
DROP procedure IF EXISTS `Proc_2`;

DELIMITER $$
USE `storedProc`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `Proc_2`()
BEGIN
	DO SLEEP(2);
	SELECT 2 as `output`;
END$$

DELIMITER ;


USE `storedProc`;
DROP procedure IF EXISTS `storedProc`.`new_procedure`;

DELIMITER $$
USE `storedProc`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `Proc_3`()
BEGIN
	DO SLEEP(3);
	SELECT 3 as `output`;
END$$

DELIMITER ;

USE `storedProc`;
DROP procedure IF EXISTS `Proc_4`;

DELIMITER $$
USE `storedProc`$$
CREATE PROCEDURE `Proc_4` ()
BEGIN
	DO SLEEP(4);
	SELECT 4 as `output`;
END$$

DELIMITER ;





