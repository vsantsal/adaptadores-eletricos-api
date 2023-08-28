package com.example.adaptadoreseletricos.domain.entity.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EletrodomesticosPessoasTest {


    private final Pessoa pessoaStub = new Pessoa();

    private final Eletrodomestico eletrodomesticoStub = new Eletrodomestico();

    @DisplayName("Teste relacionamento de residência está ativo ao inicializar")
    @Test
    public void test_estah_ativo_ao_inicializar(){
        // Arrange
        EletrodomesticosPessoas eletrodomesticosPessoas = new EletrodomesticosPessoas(
                pessoaStub, eletrodomesticoStub);

        // Act
        boolean isAtivo = eletrodomesticosPessoas.isAtivo();

        // Assert
        assertTrue(isAtivo);

    }

    @DisplayName("Teste data de ativação corresponde a data de inicialização do relacionamento")
    @Test
    public void test_data_de_atualizacao_apos_inicializacao_corresponde_a_hoje(){
        // Arrange
        EletrodomesticosPessoas eletrodomesticosPessoas = new EletrodomesticosPessoas(
                pessoaStub, eletrodomesticoStub);

        // Act
        LocalDate dataAtualizacao = eletrodomesticosPessoas.getDataAtualizacao();

        // Assert
        assertEquals(LocalDate.now(), dataAtualizacao);

    }

    @DisplayName("Teste relacionamento de residência está inativo ao ser desativado")
    @Test
    public void test_estah_inativo_ao_ser_desativado(){
        // Arrange
        EletrodomesticosPessoas eletrodomesticosPessoas = new EletrodomesticosPessoas(
                pessoaStub, eletrodomesticoStub);
        eletrodomesticosPessoas.desativar();

        // Act
        boolean isAtivo = eletrodomesticosPessoas.isAtivo();

        // Assert
        assertFalse(isAtivo);

    }

}