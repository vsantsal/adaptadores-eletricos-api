alter table pessoa
    ADD CONSTRAINT unicidade_pessoa UNIQUE(nome, data_nascimento, sexo, parentesco);