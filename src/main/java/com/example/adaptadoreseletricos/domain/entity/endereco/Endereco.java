package com.example.adaptadoreseletricos.domain.entity.endereco;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
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

    @OneToMany(mappedBy = "endereco")
    private Set<Eletrodomestico> eletrodomesticos;

    public Endereco(Long id, String rua, Long numero,
                    String bairro, String cidade, Estado estado){
        this.id = id;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

}
