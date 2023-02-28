CREATE TABLE users
(
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    enabled  BOOLEAN DEFAULT true,
    PRIMARY KEY (username)
);

comment on table users is 'Зарегистрированные в приложении пользователи';
comment on column users.username is 'Имя пользователя - первичный ключ';
comment on column users.password is 'Пароль пользователя';
comment on column users.enabled is 'Доступ пользователя в приложение';

CREATE TABLE authorities
(
    username VARCHAR NOT NULL,
    authority VARCHAR NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username)
);

comment on table authorities is 'Роли пользователей';
comment on column authorities.username is 'Имя пользователя - внешний ключ';
comment on column authorities.authority is 'Роль пользователя';