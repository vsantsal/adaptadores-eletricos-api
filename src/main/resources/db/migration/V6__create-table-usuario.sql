create table usuario(
    id bigint NOT NULL auto_increment,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    pessoa_id bigint NOT NULL,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
    PRIMARY KEY (id)
);