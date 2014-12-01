ALTER TABLE PupilInfo ADD CONSTRAINT BreakfastPortion_Portion
FOREIGN KEY (breakfastPortion_id) REFERENCES Portion (id);

ALTER TABLE PupilInfo ADD CONSTRAINT DinnertPortion_Portion
FOREIGN KEY (dinnerPortion_id) REFERENCES Portion (id);