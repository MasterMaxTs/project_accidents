CREATE TABLE IF NOT EXISTS accidents_rules
(
    accident_id INT REFERENCES accidents (id),
    rule_id INT REFERENCES rules (id)
);


comment on table accidents_rules is 'Объединяющая таблица для автоинцидентов и статей автонарушений';
comment on column accidents_rules.accident_id is 'Идентификатор автоинцидента - внешний ключ';
comment on column accidents_rules.rule_id is 'Идентификатор статьи автонарушения - внешний ключ';
