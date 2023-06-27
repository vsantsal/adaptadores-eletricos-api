create table eletrodomestico (
    id bigint NOT NULL auto_increment,
    nome VARCHAR(120) NOT NULL,
    modelo VARCHAR(120) NOT NULL,
    marca VARCHAR(120) NOT NULL,
    potencia bigint NOT NULL,
    primary key(id)
);