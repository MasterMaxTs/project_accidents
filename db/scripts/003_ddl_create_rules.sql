CREATE TABLE IF NOT EXISTS rules
(
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);


comment on table rules is 'Статьи автонарушений';
comment on column rules.id is 'Идентификатор статьи';
comment on column rules.name is 'Описание статьи';