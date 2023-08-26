package com.example.adaptadoreseletricos.domain.entity.pessoa;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ParentescoPessoasChave implements Serializable {
    @Column(name = "id_pessoa1")
    Long idPessoa1;

    @Column(name = "id_pessoa2")
    Long idPessoa2;

}
