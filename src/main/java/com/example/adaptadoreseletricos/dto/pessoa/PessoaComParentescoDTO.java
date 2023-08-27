package com.example.adaptadoreseletricos.dto.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Parentesco;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;

import java.time.LocalDate;

public record PessoaComParentescoDTO(

        Long id,
        String nome,

        LocalDate dataNascimento,

        String sexo,
        String parentesco
) {
    public PessoaComParentescoDTO(Pessoa pessoa, Parentesco parentesco) {
        this(pessoa.getId(), pessoa.getNome(), pessoa.getDataNascimento(),
                pessoa.getSexo().name(), parentesco != null ? parentesco.name() : "");
    }
}
