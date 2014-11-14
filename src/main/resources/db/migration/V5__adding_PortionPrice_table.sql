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

ALTER TABLE PupilInfo ADD COLUMN breakfastPortion_id BIGINT(20) NOT NULL;
ALTER TABLE PupilInfo ADD COLUMN dinnerPortion_id BIGINT(20) NOT NULL;
UPDATE PupilInfo SET breakfastPortion_id = 1;
UPDATE PupilInfo SET dinnerPortion_id = 2;

ALTER TABLE MealEvent ADD COLUMN price BIGINT(20) NOT NULL;
ALTER TABLE MealEvent ADD COLUMN type VARCHAR(255) NOT NULL;
UPDATE MealEvent SET type = 'Nenurodyta' WHERE type = '';

