package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.service.endereco.EnderecoService;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class EnderecoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private EnderecoService service;

    @MockBean
    private EnderecoRepository repository;

    @DisplayName("Teste de cadastro de endereço válido na API")
    @WithMockUser(username = "tester")
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
                        .with(csrf())
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
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_estado_invalido() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                post("/enderecos")
                        .with(csrf())
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
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_provoca_erro_integridade_dados() throws Exception {
        // Arrange
        when(repository.save(any(Endereco.class))).thenThrow(
                DataIntegrityViolationException.class
        );

        // Act
        this.mockMvc.perform(
                post("/enderecos")
                        .with(csrf())
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
    @WithMockUser(username = "tester")
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
        this.mockMvc.perform(get("/enderecos/1")
                .with(csrf()))
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
    @WithMockUser(username = "tester")
    @Test
    public void test_nao_deve_detalhar_endereco_para_id_invalido() throws Exception {
        // Arrange
        when(repository.getReferenceById(2L)).thenThrow(
                EntityNotFoundException.class
        );

        // Act
        this.mockMvc.perform(get("/enderecos/2")
                .with(csrf()))

            // Assert
            .andExpect(status().isNotFound());


    }

    @DisplayName("Teste de inclusão de endereço com rua muito comprida retorna erro")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_nome_rua_muito_comprida() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post("/enderecos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"rua\": \""+ "x".repeat(121)+"\", " +
                                                "\"numero\": 107, " +
                                                "\"bairro\": \"Ipanema\", " +
                                                "\"cidade\": \"Rio de Janeiro\", " +
                                                "\"estado\": \"RJ\"}"
                                )
                )
                // Assert
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("Teste de inclusão de endereço com rua com número negativo retorna erro")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_numero_negativo() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post("/enderecos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"rua\": \"Rua Nascimento Silva\", " +
                                                "\"numero\": -107, " +
                                                "\"bairro\": \"Ipanema\", " +
                                                "\"cidade\": \"Rio de Janeiro\", " +
                                                "\"estado\": \"RJ\"}"
                                )
                )
                // Assert
                .andExpect(status().is4xxClientError());
    }

    @Disabled("WIP: será reavaliado quando voltarmos ao endpoint")
    @DisplayName("Teste de inclusão de endereço com sigla de estado muito comprido retorna erro")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_sigla_estado_muito_comprido() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post("/enderecos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"rua\": \"Rua Nascimento Silva\", " +
                                                "\"numero\": 107, " +
                                                "\"bairro\": \"Ipanema\", " +
                                                "\"cidade\": \"Rio de Janeiro\", " +
                                                "\"estado\": \"" + "X".repeat(3) + "\"}"
                                )
                )
                // Assert
                .andExpect(status().is4xxClientError());
    }

}