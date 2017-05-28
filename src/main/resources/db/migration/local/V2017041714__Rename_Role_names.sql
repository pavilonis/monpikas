UPDATE Role SET name = REPLACE(name, 'ROLE_', '');

UPDATE Role SET name = 'USERS_SYSTEM' WHERE name = 'ROLES';