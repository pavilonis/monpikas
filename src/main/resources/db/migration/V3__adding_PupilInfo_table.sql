CREATE TABLE `PupilInfo` (
  `cardId` BIGINT(20) NOT NULL,
  `dinnerPermitted` BIT (1) NOT NULL DEFAULT 0,
  `breakfastPermitted` BIT(1) NOT NULL DEFAULT 0,
  `comment` LONGTEXT
            COLLATE utf8_unicode_ci,
  PRIMARY KEY (`cardId`)
);