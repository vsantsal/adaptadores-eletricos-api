package com.example.adaptadoreseletricos.domain.entity.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class EletrodomesticosPessoasChave implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa", nullable = false, insertable = false, updatable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_eletrodomestico", nullable = false, insertable = false, updatable = false)
    private Eletrodomestico eletrodomestico;

    public EletrodomesticosPessoasChave(Pessoa pessoa, Eletrodomestico eletrodomestico) {
        this.pessoa = pessoa;
        this.eletrodomestico = eletrodomestico;
    }

    @Deprecated
    public EletrodomesticosPessoasChave(){

    }

}
