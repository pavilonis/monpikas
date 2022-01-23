CREATE TABLE LoginEvent (
    id         BIGINT(20)                           NOT NULL                AUTO_INCREMENT,
    created    DATETIME                             NOT NULL DEFAULT NOW(),
    name       VARCHAR(255) COLLATE utf8_general_ci,
    address    VARCHAR(255)                                                 COLLATE utf8_general_ci,
    success    BOOLEAN                              NOT NULL,
    logout     BOOLEAN                              NOT NULL,
    PRIMARY KEY (id),
    INDEX INDEX_CREATED (created)
) DEFAULT CHARSET = utf8
   COLLATE utf8_general_ci;
