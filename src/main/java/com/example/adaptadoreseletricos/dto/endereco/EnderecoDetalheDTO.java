package com.example.adaptadoreseletricos.dto.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;

public record EnderecoDetalheDTO(
        Long id,
        String rua,
        Long numero,
        String bairro,
        String cidade,
        String estado
) {
    public EnderecoDetalheDTO(Endereco endereco){
        this(endereco.getId(), endereco.getRua(), endereco.getNumero(),
                endereco.getBairro(), endereco.getCidade(), endereco.getEstado().toString());

    }
}
