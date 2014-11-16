CREATE TABLE `Portion` (
  `id`    BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(255) NOT NULL,
  `type`  VARCHAR(255) NOT NULL,
  `price` DOUBLE       NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO Portion (name, type, price) VALUES ('Pusryčiai', 'BREAKFAST', 3.00);
INSERT INTO Portion (name, type, price) VALUES ('Pradinukai', 'DINNER', 4.00);
INSERT INTO Portion (name, type, price) VALUES ('5-12', 'DINNER', 4.60);

ALTER TABLE PupilInfo ADD COLUMN breakfastPortion_id BIGINT(20) DEFAULT NULL;
ALTER TABLE PupilInfo ADD COLUMN dinnerPortion_id BIGINT(20) DEFAULT NULL;
ALTER TABLE PupilInfo ADD COLUMN grade VARCHAR(3) DEFAULT NULL;

ALTER TABLE MealEvent ADD COLUMN price BIGINT(20) NOT NULL;
ALTER TABLE MealEvent ADD COLUMN type VARCHAR(255) NOT NULL;

UPDATE MealEvent
SET type = 'Nenurodyta'
WHERE type = '';

UPDATE PupilInfo
SET breakfastPortion_id = 1
WHERE breakfastPermitted = TRUE;

UPDATE PupilInfo
SET dinnerPortion_id = 2
WHERE dinnerPermitted = TRUE;

ALTER TABLE PupilInfo DROP COLUMN breakfastPermitted;
ALTER TABLE PupilInfo DROP COLUMN dinnerPermitted;

ALTER TABLE PupilInfo MODIFY comment LONGTEXT DEFAULT NULL;
UPDATE PupilInfo
SET comment = NULL
WHERE comment = '';