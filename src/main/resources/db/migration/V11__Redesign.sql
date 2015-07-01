DROP TABLE Message;

ALTER TABLE MealEvent CHANGE type mealType VARCHAR(16) NOT NULL;
ALTER TABLE MealEvent ADD COLUMN pupilType VARCHAR(16) NOT NULL;
UPDATE MealEvent SET pupilType = 'SOCIAL';
RENAME TABLE MealEvent TO MealEventLog;
ALTER TABLE MealEventLog CHANGE Grade grade VARCHAR(3) DEFAULT NULL;

RENAME TABLE PupilInfo TO Pupil;
ALTER TABLE Pupil ADD COLUMN type VARCHAR(16) NOT NULL;
UPDATE Pupil SET type = 'SOCIAL';

RENAME TABLE Portion TO Meal;
ALTER TABLE Meal ADD COLUMN startTime TIMESTAMP NOT NULL;
ALTER TABLE Meal ADD COLUMN endTime TIMESTAMP NOT NULL;

UPDATE Meal SET startTime = now();
UPDATE Meal SET endTime = now();

CREATE TABLE Pupil_Meal (
   Pupil_cardId BIGINT(20) NOT NULL,
   meals_id   BIGINT(20) NOT NULL,

   PRIMARY KEY (Pupil_cardId, meals_id),
   FOREIGN KEY (meals_id) REFERENCES Meal (id),
   FOREIGN KEY (Pupil_cardId) REFERENCES Pupil (cardId)
);