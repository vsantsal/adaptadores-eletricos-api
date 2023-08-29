package com.example.adaptadoreseletricos.dto.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;

public record EletrodomesticoDetalheDTO(
        Long id,
        String nome,
        String modelo,
        String marca,
        Long potencia,
        Long idEndereco
) {
    public EletrodomesticoDetalheDTO(Eletrodomestico eletrodomestico){

        this(eletrodomestico.getId(), eletrodomestico.getNome(),
                eletrodomestico.getModelo(),
                eletrodomestico.getMarca(),
                eletrodomestico.getPotencia(),
                eletrodomestico.getEndereco() != null ?
                        eletrodomestico.getEndereco().getId() :
                        null);
    }
}
