package com.example.adaptadoreseletricos.domain.entity.pessoa;


import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class ParentescoPessoasChave implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa1", nullable = false, insertable = false, updatable = false)
    Pessoa pessoa1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa2", nullable = false, insertable = false, updatable = false)
    Pessoa pessoa2;

    public ParentescoPessoasChave(Pessoa pessoa1, Pessoa pessoa2) {
        this.pessoa1 = pessoa1;
        this.pessoa2 = pessoa2;
    }

    @Deprecated
    public ParentescoPessoasChave(){

    }
}
