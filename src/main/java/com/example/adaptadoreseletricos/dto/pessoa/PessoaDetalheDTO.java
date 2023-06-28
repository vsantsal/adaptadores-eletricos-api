package com.example.adaptadoreseletricos.dto.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;

import java.time.LocalDate;

public record PessoaDetalheDTO(
        Long id,

        String nome,

        LocalDate dataNascimento,

        String sexo,

        String parentesco
) {
    public PessoaDetalheDTO(Pessoa pessoa){

        this(pessoa.getId(), pessoa.getNome(), pessoa.getDataNascimento(),
                pessoa.getSexo().toString(), pessoa.getParentesco().toString());
    }
}
