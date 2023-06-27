create table endereco (
    id bigint NOT NULL auto_increment,
    rua VARCHAR(255) NOT NULL,
    numero bigint NOT NULL,
    bairro VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    estado CHAR(2) NOT NULL,
    primary key(id)
);