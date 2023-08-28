package com.example.adaptadoreseletricos.domain.entity.endereco;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enderecos_pessoas")
@Getter
@NoArgsConstructor
public class EnderecosPessoas {
    @EmbeddedId
    private EnderecosPessoasChave id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa", nullable = false, insertable = false, updatable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_endereco", nullable = false, insertable = false, updatable = false)
    private Endereco endereco;

    @Column(name = "ativo")
    private boolean ativo;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    public  EnderecosPessoas(Pessoa pessoa, Endereco endereco) {
        this.id = new EnderecosPessoasChave(pessoa, endereco);
        this.pessoa = pessoa;
        this.endereco = endereco;
        this.ativo = true;
        this.dataAtualizacao = LocalDate.now();
    }

    public void desativar() {
        this.ativo = false;
        this.dataAtualizacao = LocalDate.now();
    }
}
