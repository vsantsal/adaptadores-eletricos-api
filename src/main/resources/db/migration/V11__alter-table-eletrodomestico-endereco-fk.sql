ALTER TABLE eletrodomestico
  ADD COLUMN id_endereco bigint not null,
  ADD FOREIGN KEY fk_endereco(id_endereco) REFERENCES endereco(id) ON DELETE CASCADE;
