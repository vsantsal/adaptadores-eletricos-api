package com.example.adaptadoreseletricos.domain.entity.pessoa;

import jakarta.persistence.*;

@Entity
public class ParentescoPessoas {
    @EmbeddedId
    ParentescoPessoasChave id;

    @ManyToOne
    @MapsId("pessoaId")
    @JoinColumn(name = "id_pessoa1")
    Pessoa pessoa1;

    @ManyToOne
    @MapsId("pessoaId")
    @JoinColumn(name = "id_pessoa2")
    Pessoa pessoa2;

    @Enumerated(EnumType.STRING)
    private Parentesco parentesco;
}
