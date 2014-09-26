INSERT INTO monpikasdb.User (id, enabled, name, password, phone, username)
VALUES (1, 1, 'Darius Jucys', 'honda', '555', 'darius');
INSERT INTO monpikasdb.User (id, enabled, name, password, phone, username)
VALUES (2, 1, 'Arturas Pavilonis', '417269', '777', 'arturas');

INSERT INTO monpikasdb.Authority (id, role)
VALUES (1, 'ROLE_EMPLOYEE');
INSERT INTO monpikasdb.Authority (id, role)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO monpikasdb.User_Authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO monpikasdb.User_Authority (user_id, authority_id) VALUES (2, 2);