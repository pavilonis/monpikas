# Rename existing
RENAME TABLE User TO User2;
RENAME TABLE UserRole TO UserRole2;

# Create new
CREATE TABLE Role (
   id   BIGINT(20)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(255) NOT NULL
);
ALTER TABLE Role ADD CONSTRAINT UNIQUE_role_name UNIQUE (name);

CREATE TABLE User (
   id       BIGINT(20)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
   enabled  BIT(1)       NOT NULL DEFAULT b'1',
   name     VARCHAR(255),
   email    VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   username VARCHAR(255) NOT NULL,
   UNIQUE username (username)
);

CREATE TABLE UserRole (
   user_id BIGINT(20) NOT NULL,
   role_id BIGINT(20) NOT NULL
);
ALTER TABLE UserRole ADD FOREIGN KEY FK_UserRole_User (role_id) REFERENCES Role (id);
ALTER TABLE UserRole ADD FOREIGN KEY FK_UserRole_Role (user_id) REFERENCES User (id);

# Move data
INSERT INTO Role (name)
   SELECT DISTINCT name
   FROM UserRole2
   ORDER BY name;

INSERT INTO User (username, name, email, password, enabled)
   SELECT
      username,
      name,
      email,
      password,
      enabled
   FROM User2
   ORDER BY username;

INSERT INTO UserRole (user_id, role_id)
   SELECT
      u.id,
      r.id
   FROM User u
      JOIN User u2 ON u2.username = u.username
      JOIN UserRole2 ur2 ON ur2.username = u2.username
      JOIN Role r ON r.name = ur2.name;

DROP TABLE UserRole2;
DROP TABLE User2;

