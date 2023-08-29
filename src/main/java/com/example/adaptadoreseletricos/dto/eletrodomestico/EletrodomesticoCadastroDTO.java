package com.example.adaptadoreseletricos.dto.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EletrodomesticoCadastroDTO(
        @NotBlank @Size(min = 1, max = 120 ,message = "deve ter de 1 a 120 caracteres")
        String nome,
        @NotBlank @Size(min = 1, max = 120 ,message = "deve ter de 1 a 120 caracteres")
        String modelo,
        @NotBlank @Size(min = 1, max = 120 ,message = "deve ter de 1 a 120 caracteres")
        String marca,
        @NotNull @Positive(message = "potencia deve ser número inteiro positivo")
        Long potencia,

        @NotNull @Positive
        Long idEndereco
) {
    public Eletrodomestico toEletrodomestico(){
        Endereco endereco = new Endereco();
        endereco.setId(idEndereco);
        Eletrodomestico eletrodomestico = new Eletrodomestico();
        eletrodomestico.setNome(nome());
        eletrodomestico.setMarca(marca());
        eletrodomestico.setModelo(modelo());
        eletrodomestico.setPotencia(potencia());
        eletrodomestico.setEndereco(endereco);

        return eletrodomestico;
    }
}
