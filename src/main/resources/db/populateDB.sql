DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (description, date_time, calories, user_id)
VALUES ('Завтрак', '2020-01-30 10:00', 500, 100000),
       ('Обед', '2020-01-30 13:00', 1000, 100000),
       ('Ужин', '2020-01-30 20:00', 500, 100000),
       ('Еда на границе', '2020-01-31 00:00', 100, 100001),
       ('Завтрак', '2020-01-30 10:00', 1000, 100001),
       ('Обед', '2020-01-31 13:00', 500, 100001),
       ('Ужин2', '2020-01-30 19:00', 410, 100001),
       ('Завтрак на траве', '2020-01-27 07:00', 210, 100001),
       ('Полдник', '2020-01-28 15:00', 210, 100001),
       ('Поздний ланч', '2020-01-29 00:00', 210, 100001);