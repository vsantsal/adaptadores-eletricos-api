package com.example.adaptadoreseletricos.dto;

public record CadastroEnderecoDTO(
        String rua,
        Long numero,
        String bairro,
        String cidade,
        String estado
) {
}
