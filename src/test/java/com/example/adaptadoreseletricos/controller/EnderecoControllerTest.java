package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.service.endereco.EnderecoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


}