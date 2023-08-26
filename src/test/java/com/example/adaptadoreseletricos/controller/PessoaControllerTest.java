package com.example.adaptadoreseletricos.controller;


import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.service.pessoa.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PessoaControllerTest {

    private final String ENDPOINT = "/pessoas";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private PessoaService service;

    @MockBean
    private PessoaRepository repository;

    @DisplayName("Teste de cadastro de pessoa com dados válidos na API")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_criar_pessoa_se_dados_informados_validos() throws Exception {
        // Arrange
        when(repository.save(any(Pessoa.class))).thenReturn(
                new Pessoa(
                        1L,
                        "F".repeat(120),
                        LocalDate.of(1980, 1, 1),
                        Sexo.MASCULINO
                )
        );

        // Act
        this.mockMvc.perform(
                post(ENDPOINT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"nome\": \"" + "F".repeat(120) + "\", " +
                                        "\"dataNascimento\": \"1980-01-01\", " +
                                        "\"sexo\": \"MASCULINO\"}"
                        )
        )
                // Asset
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(ENDPOINT + "/1")));
    }

    @DisplayName("Teste de cadastro de pessoa com sexo inválido")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_sexo_invalido() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                post(ENDPOINT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"nome\": \"Fulano de Tal\", " +
                                        "\"dataNascimento\": \"1980-01-01\", " +
                                        "\"sexo\": \"INEXISTENTE\"}"
                        )
        )

        // Assert
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("Teste de cadastro de pessoa com nome acima 120 caracteres retorna erro")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_nome_pessoa_muito_comprido() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                post(ENDPOINT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"nome\": \"" + "Z".repeat(121) + "\", " +
                                        "\"dataNascimento\": \"1980-01-01\", " +
                                        "\"sexo\": \"MASCULINO\"}"
                        )
        )
                // Assert
                .andExpect(status().is4xxClientError());

    }

    @DisplayName("Teste de detalhamento de pessoa para id válido na API")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_detalhar_pessoa_para_id_valido() throws Exception {
        // Arrange
        when(repository.getReferenceById(1L)).thenReturn(
                new Pessoa(
                        1L,
                        "Ciclana de Só",
                        LocalDate.of(1980, 1, 1),
                        Sexo.FEMININO
                )
        );

        // Act
        this.mockMvc.perform(get(ENDPOINT +"/1")
                .with(csrf()))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$.nome",
                        Matchers.is("Ciclana de Só")))
                .andExpect(jsonPath("$.dataNascimento",
                        Matchers.is("1980-01-01")))
                .andExpect(jsonPath("$.sexo",
                        Matchers.is("FEMININO")));
    }

    @DisplayName("Teste de detalhamento de pessoa para Id inexistente na API")
    @WithMockUser(username = "tester")
    @Test
    public void test_nao_deve_detalhar_pessoa_para_id_invalido() throws Exception {
        // Arrange
        when(repository.getReferenceById(2L)).thenThrow(
                EntityNotFoundException.class
        );

        // Act
        this.mockMvc.perform(get(ENDPOINT + "/2")
                        .with(csrf()))

                // Assert
                .andExpect(status().isNotFound());

    }

    @DisplayName("Teste com erro de integridade de dados no BD")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_provoca_erro_integridade_dados() throws Exception {
        // Arrange
        when(repository.save(any(Pessoa.class))).thenThrow(
                DataIntegrityViolationException.class
        );

        // Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Fulano de Tal\", " +
                                                "\"dataNascimento\": \"1980-01-01\", " +
                                                "\"sexo\": \"MASCULINO\" }"
                                )
                )
                // Assert
                .andExpect(status().isConflict());
    }
}