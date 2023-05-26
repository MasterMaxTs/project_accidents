CREATE TABLE IF NOT EXISTS accidents
(
    id           SERIAL,
    car_plate    VARCHAR NOT NULL,
    name         VARCHAR NOT NULL,
    type_id      INT     NOT NULL,
    text         TEXT    NOT NULL,
    address      VARCHAR NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated      TIMESTAMP WITHOUT TIME ZONE,
    resolution   TEXT,
    notice TEXT,
    user_id      INT     NOT NULL,
    status_id    INT     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (type_id) REFERENCES types (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (status_id) REFERENCES statuses (id)
);


comment on table accidents is 'Автоинциденты';
comment on column accidents.id is 'Идентификатор автоинцидента - первичный ключ';
comment on column accidents.car_plate is 'Регистрационный знак автомобиля';
comment on column accidents.type_id is ' Идентификатор типа автоинцидента - внешний ключ';
comment on column accidents.text is 'Описание автоинцидента';
comment on column accidents.address is 'Адрес места автопроисшествия';
comment on column accidents.created is 'Локальное время регистрации автоинцидента';
comment on column accidents.updated is 'Локальное время обновления информации автоинцидента';
comment on column accidents.resolution is 'Постановление о ДТП';
comment on column accidents.notice is 'Уведомление инициатору автоинцидента';
comment on column accidents.user_id is 'Идентификатор пользователя (автора инцидента) - внешний ключ';
comment on column accidents.status_id is 'Идентификатор статуса сопровождения автоинцидента - внешний ключ';
