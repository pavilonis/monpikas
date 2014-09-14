INSERT INTO monpikasdb.User (id, enabled, name, password, phone, username)
VALUES (1, 1, 'Pirmas Darbuotojas', 'user1', '555', 'user1');
INSERT INTO monpikasdb.User (id, enabled, name, password, phone, username)
VALUES (2, 1, 'Antras Darbuotojas', 'user2', '333', 'user2');
INSERT INTO monpikasdb.User (id, enabled, name, password, phone, username)
VALUES (3, 1, 'Adminas Adminovas', 'admin', '777', 'admin');

INSERT INTO monpikasdb.Authority (id, role)
VALUES (1, 'ROLE_EMPLOYEE');
INSERT INTO monpikasdb.Authority (id, role)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO monpikasdb.User_Authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO monpikasdb.User_Authority (user_id, authority_id) VALUES (2, 1);
INSERT INTO monpikasdb.User_Authority (user_id, authority_id) VALUES (3, 1);
INSERT INTO monpikasdb.User_Authority (user_id, authority_id) VALUES (3, 2);