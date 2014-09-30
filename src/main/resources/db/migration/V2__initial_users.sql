INSERT INTO User (id, enabled, name, password, phone, username) VALUES (1, 1, 'Darius Jucys', 'honda', '777', 'darius');
INSERT INTO User (id, enabled, name, password, phone, username) VALUES (2, 1, 'Arturas Pavilonis', '417269', '777', 'arturas');
INSERT INTO User (id, enabled, name, password, phone, username) VALUES (3, 1, 'Scanner', 'DinnerScan', '777', 'Scanner');

INSERT INTO Authority (id, role) VALUES (1, 'ROLE_EMPLOYEE');
INSERT INTO Authority (id, role) VALUES (2, 'ROLE_ADMIN');
INSERT INTO Authority (id, role) VALUES (3, 'ROLE_SCANNER');

INSERT INTO User_Authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO User_Authority (user_id, authority_id) VALUES (2, 2);
INSERT INTO User_Authority (user_id, authority_id) VALUES (3, 3);