package com.example.adaptadoreseletricos.domain.entity.endereco;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rua;

    private Long numero;

    private String bairro;

    private String cidade;

    @Enumerated(EnumType.STRING)
    private Estado estado;


}
