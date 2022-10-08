INSERT INTO RESTAURANTS (NAME, EMAIL)
VALUES ('ASTORIA', 'astoria@yandex.ru'),
       ('CONTINENTAL', 'continental@yandex.ru'),
       ('PRAGUE', 'prague@gmail.com');

INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO VOTES (RESTAURANT_ID, USER_ID)
VALUES (1, 1),
       (1, 2);

-- INSERT INTO VOTE (ID, RESTAURANT_ID)
-- VALUES (1, 1),
--        (2, 1);

INSERT INTO DISHES (NAME, PRICE, RESTAURANT_ID)
VALUES ('Escalope', 2.5, 1),
       ('Grilled chicken', 1.1, 1),
       ('Marinated squid', 0.5, 1),
       ('Scrambled eggs', 0.9, 2),
       ('Vegetable stew', 1.05, 2),
       ('Italian pasta', 0.6, 2),
       ('Sponge cake', 3.1, 3),
       ('Coconut ice cream', 2.1, 3),
       ('Coffee with milk', 0.25, 3);