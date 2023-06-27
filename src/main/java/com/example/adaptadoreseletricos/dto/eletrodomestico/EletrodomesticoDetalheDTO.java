package com.example.adaptadoreseletricos.dto.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;

public record EletrodomesticoDetalheDTO(
        Long id,
        String nome,
        String modelo,
        String marca,
        Long potencia
) {
    public EletrodomesticoDetalheDTO(Eletrodomestico eletrodomestico){
        this(eletrodomestico.getId(), eletrodomestico.getNome(),
                eletrodomestico.getModelo(), eletrodomestico.getMarca(),
                eletrodomestico.getPotencia());
    }
}
