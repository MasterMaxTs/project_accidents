CREATE TABLE users
(
    id SERIAL,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    enabled BOOLEAN DEFAULT true,
    authority_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (username),
    FOREIGN KEY (authority_id) REFERENCES authorities (id)
);

comment on table users is 'Зарегистрированные в приложении пользователи';
comment on column users.id is 'Идентификатор пользователя - первичный ключ';
comment on column users.username is 'Имя пользователя';
comment on column users.password is 'Пароль пользователя';
comment on column users.enabled is 'Доступ пользователя в приложение';
comment on column users.authority_id is 'Идентификатор роли - внешний ключ'