CREATE TABLE users
(
    id           SERIAL,
    username     VARCHAR NOT NULL,
    password     VARCHAR NOT NULL,
    email        VARCHAR NOT NULL,
    enabled      BOOLEAN                     DEFAULT true,
    created      TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated      TIMESTAMP WITHOUT TIME ZONE,
    authority_id INT     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (authority_id) REFERENCES authorities (id),
    CONSTRAINT users_username_unique UNIQUE (username),
    CONSTRAINT users_email_unique UNIQUE (email)
);

comment on table users is 'Зарегистрированные в приложении пользователи';
comment on column users.id is 'Идентификатор пользователя - первичный ключ';
comment on column users.username is 'Имя пользователя - уникальное значение';
comment on column users.password is 'Пароль пользователя';
comment on column users.email is 'Email пользователя - уникальное значение';
comment on column users.enabled is 'Доступ пользователя в приложение';
comment on column users.created is 'Локальное время регистрации пользователя в приложении';
comment on column users.updated is 'Локальное время обновления учётных данных пользователя в приложении';
comment on column users.authority_id is 'Идентификатор роли - внешний ключ';
