package com.example.adaptadoreseletricos.domain.entity.pessoa;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parentesco_pessoas")
@NoArgsConstructor
public class ParentescoPessoas {
    @EmbeddedId
    ParentescoPessoasChave id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa1", nullable = false, insertable = false, updatable = false)
    Pessoa pessoa1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa2", nullable = false, insertable = false, updatable = false)
    Pessoa pessoa2;

    @Enumerated(EnumType.STRING)
    private Parentesco parentesco;

    public ParentescoPessoas(Pessoa pessoa1, Pessoa pessoa2, Parentesco parentesco) {
        this.id = new ParentescoPessoasChave(pessoa1, pessoa2);
        this.pessoa1 = pessoa1;
        this.pessoa2 = pessoa2;
        this.parentesco = parentesco;
    }
}
