CREATE TABLE IF NOT EXISTS documents (
    id SERIAL,
    file_name VARCHAR NOT NULL,
    data BYTEA NOT NULL,
    author VARCHAR NOT NULL,
    accident_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (accident_id) REFERENCES accidents (id)
);

comment on table documents is  'Сопроводительные документы';
comment on column documents.id is 'Идентифиатор документа - первичный ключ';
comment on column documents.file_name is 'Имя документа';
comment on column documents.data is 'Документ в виде двоичного типа данных';
comment on column documents.author is 'Автор документа';
comment on column documents.accident_id is 'Идентификатор автоинцидента - внешний ключ';