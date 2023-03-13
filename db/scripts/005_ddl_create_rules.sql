CREATE TABLE IF NOT EXISTS rules
(
    id SERIAL,
    name TEXT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);


comment on table rules is 'Статьи автонарушений';
comment on column rules.id is 'Идентификатор статьи - первичный ключ';
comment on column rules.name is 'Описание статьи - уникальное значение';