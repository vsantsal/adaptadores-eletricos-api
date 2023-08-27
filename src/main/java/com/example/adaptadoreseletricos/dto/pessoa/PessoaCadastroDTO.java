package com.example.adaptadoreseletricos.dto.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Parentesco;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PessoaCadastroDTO(

        @NotBlank @Size(min = 1, max = 120, message = "campo deve ter entre 1 e 120 caracteres")
        String nome,
        @NotNull
        LocalDate dataNascimento,
        @NotBlank
        String sexo,

        String parentesco
) {
    public Pessoa toPessoa() {
        Pessoa pessoa = new Pessoa();
        if (nome() != null) {
            pessoa.setNome(nome());
        }
        if (dataNascimento() != null){
            pessoa.setDataNascimento(dataNascimento());
        }
        if (sexo() != null){
            pessoa.setSexo(Sexo.valueOf(sexo()));
        }
        return pessoa;
    }
}
