CREATE TABLE Role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UNIQUE_role_name (name)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;
INSERT INTO Role (name) VALUE ('SCANLOG');
INSERT INTO Role (name) VALUE ('ACTUATOR');

CREATE TABLE User (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    name VARCHAR(255) COLLATE utf8_general_ci,
    email VARCHAR(255) COLLATE utf8_general_ci,
    password VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    username VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    cardCode VARCHAR(255) COLLATE utf8_general_ci,
    birthDate DATE,
    userRole VARCHAR(255) COLLATE utf8_general_ci,
    userGroup VARCHAR(255) COLLATE utf8_general_ci,
    PRIMARY KEY (id),
    UNIQUE KEY username (username)
) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;
INSERT INTO User (username, password) VALUE ('admin', 'admin');


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

CREATE TABLE KeyLog (
    dateTime DATETIME NOT NULL DEFAULT NOW(),
    scanner_id BIGINT(20) NOT NULL,
    cardCode VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    keyNumber INTEGER NOT NULL,
    assigned BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT FK_KeyLog_Scanner FOREIGN KEY (scanner_id) REFERENCES Scanner(id) ON DELETE CASCADE
);

CREATE TABLE ScanLog (
    dateTime DATETIME NOT NULL DEFAULT NOW(),
    scanner_id BIGINT(20) NOT NULL,
    location VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    cardCode VARCHAR(255) COLLATE utf8_general_ci NOT NULL,
    CONSTRAINT FK_ScanLog_Scanner FOREIGN KEY (scanner_id) REFERENCES Scanner(id) ON DELETE CASCADE
);