package com.example.adaptadoreseletricos.dto.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;

public record CadastroEnderecoDTO(
        String rua,
        Long numero,
        String bairro,
        String cidade,
        String estado
) {
    public Endereco toEndereco() {
        Endereco endereco = new Endereco();
        endereco.setRua(rua());
        endereco.setNumero(numero());
        endereco.setBairro(bairro());
        endereco.setCidade(cidade());
        endereco.setEstado(estado());
        return endereco;
    }
}
