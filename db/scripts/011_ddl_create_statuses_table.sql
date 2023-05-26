CREATE TABLE IF NOT EXISTS statuses
(
    id   SERIAL,
    name VARCHAR NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

comment on table statuses is 'Статусы сопровождения автоинцидентов';
comment on column statuses.id is 'Идентификатор статуса - первичный ключ';
comment on column statuses.name is 'Название статуса - уникальное значение';