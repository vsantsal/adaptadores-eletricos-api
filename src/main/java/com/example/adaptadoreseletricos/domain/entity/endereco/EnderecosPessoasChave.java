package com.example.adaptadoreseletricos.domain.entity.endereco;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class EnderecosPessoasChave implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa", nullable = false, insertable = false, updatable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_endereco", nullable = false, insertable = false, updatable = false)
    private Endereco endereco;

    public EnderecosPessoasChave(Pessoa pessoa, Endereco endereco) {
        this.pessoa = pessoa;
        this.endereco = endereco;
    }

    @Deprecated
    public EnderecosPessoasChave(){
    }
}
