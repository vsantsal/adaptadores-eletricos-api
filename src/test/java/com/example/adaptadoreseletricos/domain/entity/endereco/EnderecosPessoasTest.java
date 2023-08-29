package com.example.adaptadoreseletricos.domain.entity.endereco;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EnderecosPessoasTest {

    private final Pessoa pessoaStub = new Pessoa();

    private final Endereco enderecoStub = new Endereco();

    @DisplayName("Teste relacionamento de residência está ativo ao inicializar")
    @Test
    public void test_estah_ativo_ao_inicializar(){
        // Arrange
        EnderecosPessoas enderecosPessoas = new EnderecosPessoas(
                pessoaStub, enderecoStub);

        // Act
        boolean isAtivo = enderecosPessoas.isAtivo();

        // Assert
        assertTrue(isAtivo);

    }

    @DisplayName("Teste data de ativação corresponde a data de inicialização do relacionamento")
    @Test
    public void test_data_de_atualizacao_apos_inicializacao_corresponde_a_hoje(){
        // Arrange
        EnderecosPessoas enderecosPessoas = new EnderecosPessoas(
                pessoaStub, enderecoStub);

        // Act
        LocalDate dataAtualizacao = enderecosPessoas.getDataAtualizacao();

        // Assert
        assertEquals(LocalDate.now(), dataAtualizacao);

    }

    @DisplayName("Teste relacionamento de residência está inativo ao ser desativado")
    @Test
    public void test_estah_inativo_ao_ser_desativado(){
        // Arrange
        EnderecosPessoas enderecosPessoas = new EnderecosPessoas(
                pessoaStub, enderecoStub);
        enderecosPessoas.desativar();

        // Act
        boolean isAtivo = enderecosPessoas.isAtivo();

        // Assert
        assertFalse(isAtivo);

    }

}