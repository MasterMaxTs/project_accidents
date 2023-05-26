CREATE TABLE IF NOT EXISTS accidents_rules
(
    accident_id INT NOT NULL,
    rule_id     INT NOT NULL,
    PRIMARY KEY (accident_id, rule_id),
    FOREIGN KEY (accident_id) REFERENCES accidents (id),
    FOREIGN KEY (rule_id) REFERENCES rules (id)
);


comment on table accidents_rules is 'Объединяющая таблица для автоинцидентов и статей автонарушений';
comment on column accidents_rules.accident_id is 'Идентификатор автоинцидента - внешний ключ';
comment on column accidents_rules.rule_id is 'Идентификатор статьи автонарушения - внешний ключ';
