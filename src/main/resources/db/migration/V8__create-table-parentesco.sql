create table parentesco_pessoas (
    id_pessoa1 bigint not null,
    id_pessoa2 bigint not null,
    parentesco VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_pessoa1) REFERENCES pessoa(id),
    FOREIGN KEY (id_pessoa2) REFERENCES pessoa(id),
    primary key (id_pessoa1, id_pessoa2)
);