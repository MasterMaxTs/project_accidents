CREATE TABLE IF NOT EXISTS registration_cards
(
    id                              SERIAL,
    owner_name                      VARCHAR NOT NULL,
    owner_surname                   VARCHAR NOT NULL,
    car_plate                       VARCHAR NOT NULL,
    registration_certificate_number VARCHAR NOT NULL,
    vin_code                        VARCHAR NOT NULL,
    user_id                         INT     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (car_plate),
    UNIQUE (registration_certificate_number),
    UNIQUE (vin_code)
);


comment on table registration_cards is 'Карточки учёта транспортного средства в ГИБДД';
comment on column registration_cards.id is 'Идентификатор карточки учёта - первичный ключ';
comment on column registration_cards.owner_name is 'Имя владельца автомобиля';
comment on column registration_cards.owner_surname is 'Фамилия владельца автомобиля';
comment on column registration_cards.car_plate is 'Регистрационный знак автомобиля - уникальное значение';
comment on column registration_cards.registration_certificate_number is 'Номер свидетельства регистрации автомобиля - уникальное значение';
comment on column registration_cards.vin_code is 'VIN-код автомобиля - уникальное значение';
comment on column registration_cards.user_id is 'Идентификатор владельца - внешний ключ';