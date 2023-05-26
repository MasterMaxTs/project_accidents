CREATE TABLE IF NOT EXISTS types
(
    id   SERIAL,
    name VARCHAR NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);


comment on table types is 'Типы автоинцидентов';
comment on column types.id is 'Идентификатор типа - первичный ключ';
comment on column types.name is 'Название типа - уникальное значение';