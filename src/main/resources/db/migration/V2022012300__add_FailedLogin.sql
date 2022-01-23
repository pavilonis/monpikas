CREATE TABLE FailedLogin (
    id         BIGINT(20)                           NOT NULL                AUTO_INCREMENT,
    created    DATETIME                             NOT NULL DEFAULT NOW(),
    name       VARCHAR(255) COLLATE utf8_general_ci,
    address    VARCHAR(255)                                                 COLLATE utf8_general_ci,
    PRIMARY KEY (id),
    INDEX INDEX_CREATED (created)
) DEFAULT CHARSET = utf8
   COLLATE utf8_general_ci;
