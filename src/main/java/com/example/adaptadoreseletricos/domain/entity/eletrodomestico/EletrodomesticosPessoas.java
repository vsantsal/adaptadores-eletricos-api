package com.example.adaptadoreseletricos.domain.entity.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "eletrodomesticos_pessoas")
@Getter
@NoArgsConstructor
public class EletrodomesticosPessoas {

    @EmbeddedId
    private EletrodomesticosPessoasChave id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa", nullable = false, insertable = false, updatable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_eletrodomestico", nullable = false, insertable = false, updatable = false)
    private Eletrodomestico eletrodomestico;

    @Column(name = "ativo")
    private boolean ativo;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    public EletrodomesticosPessoas(Pessoa pessoa, Eletrodomestico eletrodomestico) {
        this.id = new EletrodomesticosPessoasChave(pessoa, eletrodomestico);
        this.pessoa = pessoa;
        this.eletrodomestico = eletrodomestico;
        this.ativo = true;
        this.dataAtualizacao = LocalDate.now();
    }

    public void desativar() {
        this.ativo = false;
        this.dataAtualizacao = LocalDate.now();
    }
}
