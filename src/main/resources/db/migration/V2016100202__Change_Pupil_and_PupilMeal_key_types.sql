RENAME TABLE Pupil_Meal TO PupilMeal;

# Drop foreign keys to change key names and types
ALTER TABLE PupilMeal DROP FOREIGN KEY PupilMeal_ibfk_2;
ALTER TABLE PupilMeal DROP FOREIGN KEY PupilMeal_ibfk_1;

# Change keys
ALTER TABLE PupilMeal CHANGE Pupil_cardId pupil_cardCode VARCHAR(255) NOT NULL;
ALTER TABLE PupilMeal CHANGE meals_id meal_id BIGINT NOT NULL;
ALTER TABLE Pupil CHANGE cardId cardCode VARCHAR(255);

# Put back constraints
ALTER TABLE PupilMeal ADD CONSTRAINT FK_pupil_cardCode FOREIGN KEY (pupil_cardCode) REFERENCES Pupil (cardCode);
ALTER TABLE PupilMeal ADD CONSTRAINT FK_meal_id FOREIGN KEY (meal_id) REFERENCES Meal (id);