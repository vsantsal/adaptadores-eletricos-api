create table eletrodomesticos_pessoas (
    id_pessoa bigint not null,
    id_eletrodomestico bigint not null,
    ativo boolean NOT NULL,
    data_atualizacao DATE NOT NULL,
    FOREIGN KEY (id_pessoa) REFERENCES pessoa(id),
    FOREIGN KEY (id_eletrodomestico) REFERENCES eletrodomestico(id),
    primary key (id_pessoa, id_eletrodomestico)
);