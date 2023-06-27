alter table endereco
    ADD CONSTRAINT unicidade_endereco UNIQUE(rua, numero, cidade, estado);