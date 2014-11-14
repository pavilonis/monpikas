CREATE TABLE `Authority` (
  `id`   BIGINT(20) NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(255)
         COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `User` (
  `id`       BIGINT(20) NOT NULL AUTO_INCREMENT,
  `enabled`  BIT(1) DEFAULT NULL,
  `name`     VARCHAR(255)
             COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` VARCHAR(255)
             COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone`    VARCHAR(255)
             COLLATE utf8_unicode_ci DEFAULT NULL,
  `username` VARCHAR(255)
             COLLATE utf8_unicode_ci DEFAULT NULL,
  `usertype` VARCHAR(255)
             COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
);

CREATE TABLE `User_Authority` (
  `user_id`      BIGINT(20) NOT NULL,
  `authority_id` BIGINT(20) NOT NULL,
  KEY `FK6555336ABA004C5A` (`user_id`),
  KEY `FK6555336A66518E7F` (`authority_id`),
  CONSTRAINT `FK6555336A66518E7F` FOREIGN KEY (`authority_id`) REFERENCES `Authority` (`id`),
  CONSTRAINT `FK6555336ABA004C5A` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)
);

