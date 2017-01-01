# Temporarily store old data
CREATE TEMPORARY TABLE UserTmp
      SELECT *
      FROM User;

CREATE TEMPORARY TABLE UserRoleTmp
      SELECT
         u.username AS username,
         a.role     AS role
      FROM User u
         JOIN User_Authority ua ON u.id = ua.user_id
         JOIN Authority a ON a.id = ua.authority_id;

# Delete old tables
DROP TABLE User_Authority;
DROP TABLE User;
DROP TABLE Authority;

# Create new tables
CREATE TABLE User (
   username VARCHAR(255) NOT NULL PRIMARY KEY,
   name     VARCHAR(255),
   enabled  BIT(1)       NOT NULL DEFAULT b'1',
   password VARCHAR(255) NOT NULL
);

CREATE TABLE UserRole (
   username VARCHAR(255) NOT NULL,
   name     VARCHAR(255) NOT NULL,
   PRIMARY KEY (username, name),
   CONSTRAINT FK_UserRole_User FOREIGN KEY (username) REFERENCES User (username)
      ON DELETE CASCADE
);

# Fill table with data
INSERT INTO User (username, name, enabled, password)
   SELECT
      username,
      name,
      enabled,
      password
   FROM UserTmp;

INSERT INTO UserRole (username, name)
   SELECT
      username,
      role
   FROM
      UserRoleTmp;

# Drop TMP tables
DROP TEMPORARY TABLE UserTmp;
DROP TEMPORARY TABLE UserRoleTmp;