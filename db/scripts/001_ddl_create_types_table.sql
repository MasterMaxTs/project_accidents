CREATE TABLE IF NOT EXISTS types
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);


comment on table types is 'Типы автоинцидентов';
comment on column types.id is 'Идентификатор типа';
comment on column types.name is 'Название типа';