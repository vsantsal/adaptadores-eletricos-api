create table enderecos_pessoas (
    id_pessoa bigint not null,
    id_endereco bigint not null,
    ativo boolean NOT NULL,
    data_atualizacao DATE NOT NULL,
    FOREIGN KEY (id_pessoa) REFERENCES pessoa(id),
    FOREIGN KEY (id_endereco) REFERENCES endereco(id),
    primary key (id_pessoa, id_endereco)
);