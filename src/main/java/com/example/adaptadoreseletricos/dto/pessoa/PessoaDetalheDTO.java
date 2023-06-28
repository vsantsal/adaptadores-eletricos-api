package com.example.adaptadoreseletricos.dto.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;

public record PessoaDetalheDTO(
        Long id
) {
    public PessoaDetalheDTO(Pessoa pessoa){

        this(pessoa.getId());
    }
}
