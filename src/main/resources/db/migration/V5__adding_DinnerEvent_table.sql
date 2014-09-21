CREATE TABLE `DinnerEvent` (
  `id`     BIGINT(20) AUTO_INCREMENT,
  `cardId` BIGINT(20)   NOT NULL,
  `name`   VARCHAR(255) NOT NULL,
  `date`   TIMESTAMP    NOT NULL,
  PRIMARY KEY (`id`)
);