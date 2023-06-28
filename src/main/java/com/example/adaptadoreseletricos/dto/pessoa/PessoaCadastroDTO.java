package com.example.adaptadoreseletricos.dto.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Parentesco;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;

import java.time.LocalDate;

public record PessoaCadastroDTO(
        String nome,
        LocalDate dataNascimento,
        String sexo,
        String parentesco
) {
    public Pessoa toPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome());
        pessoa.setDataNascimento(dataNascimento());
        pessoa.setSexo(Sexo.valueOf(sexo()));
        pessoa.setParentesco(Parentesco.valueOf(parentesco()));
        return pessoa;
    }
}
