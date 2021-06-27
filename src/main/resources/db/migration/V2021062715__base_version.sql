CREATE TABLE Role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UNIQUE_role_name (name)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;
INSERT INTO Role (name) VALUES ('SCANLOG'), ('KEYS'), ('USERS_SYSTEM'), ('USERS'), ('ACTUATOR');

CREATE TABLE SystemUser (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    email VARCHAR(255) COLLATE utf8_general_ci,
    password VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    username VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    name VARCHAR(255) COLLATE utf8_general_ci,
    PRIMARY KEY (id),
    UNIQUE KEY username (username)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;

CREATE TABLE User (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) COLLATE utf8_general_ci,
    cardCode VARCHAR(255) COLLATE utf8_general_ci,
    birthDate DATE,
    organizationRole VARCHAR(255) COLLATE utf8_general_ci,
    organizationGroup VARCHAR(255) COLLATE utf8_general_ci,
    picture BLOB,
    PRIMARY KEY (id)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;
INSERT INTO User (name, cardCode, birthDate, organizationRole, organizationGroup)
VALUES ('John Smith', 'abcd1234', '2000-01-01', 'Pupil', '5A'),
('Jane Doe', 'efgh5678', '1980-01-01', 'Teacher', 'Mathematics');

CREATE TABLE UserRole (
    user_id BIGINT(20) NOT NULL,
    role_id BIGINT(20) NOT NULL,
    CONSTRAINT FK_UserRole_Role FOREIGN KEY (role_id) REFERENCES Role (id) ON DELETE CASCADE,
    CONSTRAINT FK_UserRole_User FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE
);

CREATE TABLE Scanner (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
INSERT INTO Scanner(name) VALUES ('Test scanner 1'), ('Test scanner 2');

CREATE TABLE KeyLog (
    dateTime DATETIME NOT NULL DEFAULT NOW(),
    scanner_id BIGINT(20) NOT NULL,
    cardCode VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    keyNumber INTEGER NOT NULL,
    assigned BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT FK_KeyLog_Scanner FOREIGN KEY (scanner_id) REFERENCES Scanner(id) ON DELETE CASCADE
);
INSERT INTO KeyLog (dateTime, scanner_id, cardCode, keyNumber, assigned)
VALUES (NOW(), 1, 'abcd1234', 100, TRUE), (NOW(), 2, 'efgh5678', 101, TRUE);

CREATE TABLE ScanLog (
    dateTime DATETIME NOT NULL DEFAULT NOW(),
    scanner_id BIGINT(20) NOT NULL,
    location VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    cardCode VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    CONSTRAINT FK_ScanLog_Scanner FOREIGN KEY (scanner_id) REFERENCES Scanner(id) ON DELETE CASCADE
);