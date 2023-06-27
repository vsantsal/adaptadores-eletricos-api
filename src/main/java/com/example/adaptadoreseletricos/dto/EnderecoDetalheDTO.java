package com.example.adaptadoreseletricos.dto;

public record EnderecoDetalheDTO(
        Long id,
        String rua,
        Long numero,
        String bairro,
        String cidade,
        String estado
) {
}
