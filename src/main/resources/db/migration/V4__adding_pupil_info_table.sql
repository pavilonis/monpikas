CREATE TABLE `PupilInfo` (
  `cardId` BIGINT(20) NOT NULL,
  `dinnerPermission`  BIT(1) DEFAULT NULL,
  `comment` LONGTEXT
            COLLATE utf8_unicode_ci,
  PRIMARY KEY (`cardId`)
);