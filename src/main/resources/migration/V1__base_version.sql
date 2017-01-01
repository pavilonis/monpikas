CREATE TABLE Authority (
   id   BIGINT(20)   NOT NULL AUTO_INCREMENT,
   role VARCHAR(255) NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE Meal (
   id        BIGINT(20)    NOT NULL AUTO_INCREMENT,
   name      VARCHAR(255)  NOT NULL,
   type      VARCHAR(255)  NOT NULL,
   price     DECIMAL(4, 2) NOT NULL,
   startTime DATETIME      NOT NULL,
   endTime   DATETIME      NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE MealEventLog (
   id        BIGINT(20)    NOT NULL AUTO_INCREMENT,
   cardId    BIGINT(20)    NOT NULL,
   name      VARCHAR(255)  NOT NULL,
   date      DATETIME      NOT NULL,
   price     DECIMAL(4, 2) NOT NULL,
   mealType  VARCHAR(255)  NOT NULL,
   grade     VARCHAR(255)           DEFAULT NULL,
   pupilType VARCHAR(255)  NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE Pupil (
   cardId  BIGINT(20)   NOT NULL,
   comment LONGTEXT,
   type    VARCHAR(255) NOT NULL,
   PRIMARY KEY (cardId)
);

CREATE TABLE Pupil_Meal (
   Pupil_cardId BIGINT(20) NOT NULL,
   meals_id     BIGINT(20) NOT NULL,
   PRIMARY KEY (Pupil_cardId, meals_id),
   KEY meals_id (meals_id),
   CONSTRAINT Pupil_Meal_ibfk_1 FOREIGN KEY (meals_id) REFERENCES Meal (id),
   CONSTRAINT Pupil_Meal_ibfk_2 FOREIGN KEY (Pupil_cardId) REFERENCES Pupil (cardId)
);

CREATE TABLE User (
   id       BIGINT(20)   NOT NULL AUTO_INCREMENT,
   enabled  BIT(1)       NOT NULL DEFAULT b'1',
   name     VARCHAR(255)          DEFAULT NULL,
   password VARCHAR(255) NOT NULL,
   username VARCHAR(255) NOT NULL,
   PRIMARY KEY (id),
   UNIQUE KEY username (username)
);

CREATE TABLE User_Authority (
   user_id      BIGINT(20) NOT NULL,
   authority_id BIGINT(20) NOT NULL,
   KEY FK6555336ABA004C5A (user_id),
   KEY FK6555336A66518E7F (authority_id),
   CONSTRAINT FK6555336A66518E7F FOREIGN KEY (authority_id) REFERENCES Authority (id),
   CONSTRAINT FK6555336ABA004C5A FOREIGN KEY (user_id) REFERENCES User (id)
);