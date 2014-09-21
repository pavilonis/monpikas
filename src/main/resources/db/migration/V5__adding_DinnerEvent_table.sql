CREATE TABLE `DinnerEvent` (
  `id`               BIGINT(20) AUTO_INCREMENT,
  `date`             TIMESTAMP  NOT NULL,
  `pupilInfo_cardId` BIGINT(20) NOT NULL REFERENCES PupilInfo(cardId),
  PRIMARY KEY (`id`)
);