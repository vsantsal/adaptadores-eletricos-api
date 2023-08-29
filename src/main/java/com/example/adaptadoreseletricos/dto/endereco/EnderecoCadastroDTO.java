package com.example.adaptadoreseletricos.dto.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EnderecoCadastroDTO(
        @NotBlank @Size(min = 1, max = 120, message = "campo deve possuir de 1 a 120 caracteres")
        String rua,
        @NotNull @Positive(message = "número de rua deve ser positivo")
        Long numero,
        @NotBlank @Size(min = 1, max = 120, message = "campo deve possuir de 1 a 120 caracteres")
        String bairro,
        @NotBlank @Size(min = 1, max = 120, message = "campo deve possuir de 1 a 120 caracteres")
        String cidade,
        @NotBlank @Size(min = 1, max = 2, message = "campo deve corresponder a sigla de UF")
        String estado
) {
    public Endereco toEndereco() {
        Endereco endereco = new Endereco();
        endereco.setRua(rua());
        endereco.setNumero(numero());
        endereco.setBairro(bairro());
        endereco.setCidade(cidade());
        if (estado() != null) {
            endereco.setEstado(Estado.valueOf(estado()));
        }
        return endereco;
    }
}
