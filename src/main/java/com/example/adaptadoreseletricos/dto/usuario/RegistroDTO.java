package com.example.adaptadoreseletricos.dto.usuario;

import com.example.adaptadoreseletricos.dto.pessoa.PessoaCadastroDTO;
import com.fasterxml.jackson.annotation.JsonAlias;

public record RegistroDTO(
        String login,

        String senha,

        @JsonAlias("pessoa")
        PessoaCadastroDTO pessoaCadastroDTO

) {
}
