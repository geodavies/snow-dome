CREATE DATABASE SnowDome;
USE SnowDome;

DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
  `clientid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(16) NOT NULL,
  PRIMARY KEY (`clientid`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

LOCK TABLES `clients` WRITE;
INSERT INTO `clients` VALUES (1,'snowbunny','snowbunny'),(2,'skiwidow','skiwidow'),(3,'scaredofsnow','scaredofsnow'),(4,'zoomer77','zoomer77'),(5,'doctorluz','doctorluz'),(6,'CornMeister','CornMeister'),(7,'masterJones','masterJones'),(8,'LegInTraction','LegInTraction'),(9,'Harperman','Harperman'),(10,'Harperman','Harperman');
UNLOCK TABLES;

DROP TABLE IF EXISTS `lessons`;
CREATE TABLE `lessons` (
  `description` varchar(35) NOT NULL,
  `level` smallint(6) DEFAULT NULL,
  `startDateTime` datetime NOT NULL,
  `endDateTime` datetime NOT NULL,
  `lessonid` varchar(6) NOT NULL,
  PRIMARY KEY (`lessonid`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

LOCK TABLES `lessons` WRITE;
INSERT INTO `lessons` VALUES ('Snowboarding for dummies',1,'2010-12-01 09:00:00','2010-12-01 12:00:00','L1'),('Advanced Carving Techniques',3,'2010-12-02 09:00:00','2010-12-02 14:00:00','L2'),('How to not fall off the draglift',1,'2010-12-02 14:00:00','2010-12-02 16:00:00','L3'),('Gnarliness Extreeeeeeeme',3,'2010-12-03 10:00:00','2010-12-03 13:00:00','L4'),('Parallel turns',2,'2010-12-04 09:30:00','2010-12-04 12:30:00','L5'),('How to splint a broken leg with a s',1,'2010-12-04 13:00:00','2010-12-04 15:30:00','L6'),('Cross-country techniques',2,'2010-12-05 09:30:00','2010-12-05 12:30:00','L7'),('Aerobatics',3,'2010-12-05 13:30:00','2010-12-05 16:30:00','L8'),('Intermediate Slalom ',2,'2010-12-06 09:30:00','2010-12-06 12:30:00','L9');
UNLOCK TABLES;

DROP TABLE IF EXISTS `lessons_booked`;
CREATE TABLE `lessons_booked` (
  `clientid` int(11) NOT NULL DEFAULT '1',
  `lessonid` varchar(6) NOT NULL DEFAULT '',
  PRIMARY KEY (`clientid`,`lessonid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
