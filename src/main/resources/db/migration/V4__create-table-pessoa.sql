create table pessoa (
    id bigint NOT NULL auto_increment,
    nome VARCHAR(120) NOT NULL,
    data_nascimento DATE NOT NULL,
    sexo VARCHAR(9) NOT NULL,
    parentesco VARCHAR(50) NOT NULL,
    primary key(id)
);