CREATE TABLE IF NOT EXISTS accidents
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR NOT NULL,
    type_id INT     NOT NULL REFERENCES types (id),
    text    TEXT    NOT NULL,
    address VARCHAR NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated TIMESTAMP WITHOUT TIME ZONE
);


comment on table accidents is 'Автоинциденты';
comment on column accidents.id is 'Идентификатор автоинцидента - первичный ключ';
comment on column accidents.type_id is ' Идентификатор типа автоинцидента - внешний ключ';
comment on column accidents.text is 'Описание автоинцидента';
comment on column accidents.address is 'Адрес места автопроисшествия';
comment on column accidents.created is 'Локальное время регистрации автоинцидента';
comment on column accidents.updated is 'Локальное время обновления информации автоинцидента';