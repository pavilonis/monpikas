RENAME TABLE Meal TO Eating;
RENAME TABLE MealEventLog TO EatingEvent;
RENAME TABLE PupilMeal TO PupilEating;

ALTER TABLE PupilEating DROP FOREIGN KEY FK_meal_id;
ALTER TABLE PupilEating DROP KEY meals_id;
ALTER TABLE PupilEating CHANGE meal_id eating_id BIGINT NOT NULL;
ALTER TABLE PupilEating ADD CONSTRAINT FK_eating_id FOREIGN KEY (eating_id) REFERENCES Eating (id);

ALTER TABLE EatingEvent CHANGE mealType eatingType VARCHAR(255) NOT NULL;