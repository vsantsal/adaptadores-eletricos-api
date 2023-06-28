package com.example.adaptadoreseletricos.dto.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;

public record PessoaCadastroDTO() {
    public Pessoa toPessoa() {
        return new Pessoa();
    }
}
