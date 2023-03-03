CREATE TABLE authorities
(
    id SERIAL,
    authority VARCHAR NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (authority)
);

comment on table authorities is 'Роли пользователей';
comment on column authorities.id is 'Идентификатор роли - первичный ключ';
comment on column authorities.authority is 'Роль пользователя - уникальное значение';
