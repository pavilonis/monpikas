DROP TABLE User_Authority;
DROP TABLE User;
DROP TABLE Authority;

CREATE TABLE Authority (
  id   BIGINT(20)   NOT NULL AUTO_INCREMENT,
  role VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE User (
  id       BIGINT(20)   NOT NULL AUTO_INCREMENT,
  enabled  BIT(1)       NOT NULL DEFAULT 1,
  name     VARCHAR(255) DEFAULT NULL,
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

INSERT INTO User (name, username, password) VALUES ('Scanner', 'Scanner', 'DinnerScan');
INSERT INTO User (name, username, password) VALUES ('Darius Jucys', 'darius', 'honda');
INSERT INTO User (name, username, password) VALUES ('Buhalterija', 'buh', 'hub');
INSERT INTO User (name, username, password) VALUES ('Valgykla', 'valgykla', 'batonas');

INSERT INTO Authority (role) VALUE ('ROLE_SCANNER');
INSERT INTO Authority (role) VALUE ('ROLE_EMPLOYEE');
INSERT INTO Authority (role) VALUE ('ROLE_ADMIN');

INSERT INTO User_Authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO User_Authority (user_id, authority_id) VALUES (2, 2);
INSERT INTO User_Authority (user_id, authority_id) VALUES (2, 3);
INSERT INTO User_Authority (user_id, authority_id) VALUES (3, 2);
INSERT INTO User_Authority (user_id, authority_id) VALUES (4, 2);