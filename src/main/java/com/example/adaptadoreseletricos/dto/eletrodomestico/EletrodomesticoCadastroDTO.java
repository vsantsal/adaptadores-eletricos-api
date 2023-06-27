package com.example.adaptadoreseletricos.dto.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;

public record EletrodomesticoCadastroDTO(
        String nome,
        String modelo,
        String marca,
        Long potencia
) {
    public Eletrodomestico toEletrodomestico(){
        Eletrodomestico eletrodomestico = new Eletrodomestico();
        eletrodomestico.setNome(nome());
        eletrodomestico.setMarca(marca());
        eletrodomestico.setModelo(modelo());
        eletrodomestico.setPotencia(potencia());

        return eletrodomestico;
    }
}
