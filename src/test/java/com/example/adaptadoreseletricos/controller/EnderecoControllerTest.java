package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.service.endereco.EnderecoService;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class EnderecoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private EnderecoService service;

    @MockBean
    private EnderecoRepository repository;

    @DisplayName("Teste de cadastro de endereço válido na API")
    @Test
    public void test_deve_criar_endereco_se_dados_informados_validos() throws Exception {
        // Arrange
        when(repository.save(any(Endereco.class))).thenReturn(
                new Endereco(
                        1L,
                        "Rua Nascimento Silva",
                        107L,
                        "Ipanema",
                        "Rio de Janeiro",
                        Estado.RJ
                )
        );

        // Act
        this.mockMvc.perform(
                post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"rua\": \"Rua Nascimento Silva\", " +
                                        "\"numero\": 107, " +
                                        "\"bairro\": \"Ipanema\", " +
                                        "\"cidade\": \"Rio de Janeiro\", " +
                                        "\"estado\": \"RJ\"}"
                        )
        )
                // Assert
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("enderecos/1")));
    }

    @DisplayName("Teste de inclusão de endereço com estado inválido")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_estado_invalido() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"rua\": \"Rua Nascimento Silva\", " +
                                        "\"numero\": 107, " +
                                        "\"bairro\": \"Ipanema\", " +
                                        "\"cidade\": \"Rio de Janeiro\", " +
                                        "\"estado\": \"INEXISTENTE\"}"
                        )
        )
        // Assert
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("Teste com erro de integridade de dados no BD")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_provoca_erro_integridade_dados() throws Exception {
        // Arrange
        when(repository.save(any(Endereco.class))).thenThrow(
                DataIntegrityViolationException.class
        );

        // Act
        this.mockMvc.perform(
                post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"rua\": \"Rua Nascimento Silva\", " +
                                        "\"numero\": 107, " +
                                        "\"bairro\": \"Ipanema\", " +
                                        "\"cidade\": \"Rio de Janeiro\", " +
                                        "\"estado\": \"RJ\"}"
                        )
        )
                // Assert
                .andExpect(status().isConflict());
    }

    @DisplayName("Teste de detalhamento de endereço para id válido na API")
    @Test
    public void test_deve_detalhar_endereco_para_id_valido() throws Exception {
        // Arrange
        when(repository.getReferenceById(1L)).thenReturn(
                new Endereco(
                        1L,
                        "Rua Nascimento Silva",
                        107L,
                        "Ipanema",
                        "Rio de Janeiro",
                        Estado.RJ
                )
        );

        // Act
        this.mockMvc.perform(get("/enderecos/1"))
            // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$.rua",
                        Matchers.is("Rua Nascimento Silva")))
                .andExpect(jsonPath("$.numero",
                        Matchers.is(107)))
                .andExpect(jsonPath("$.bairro",
                        Matchers.is("Ipanema")))
                .andExpect(jsonPath("$.cidade",
                        Matchers.is("Rio de Janeiro")))
                .andExpect(jsonPath("$.estado",
                        Matchers.is("RJ")));
    }

    @DisplayName("Teste de detalhamento de endereco para Id inexistente na API")
    @Test
    public void test_nao_deve_detalhar_endereco_para_id_invalido() throws Exception {
        // Arrange
        when(repository.getReferenceById(2L)).thenThrow(
                EntityNotFoundException.class
        );

        // Act
        this.mockMvc.perform(get("/enderecos/2"))

            // Assert
            .andExpect(status().isNotFound());


    }

}