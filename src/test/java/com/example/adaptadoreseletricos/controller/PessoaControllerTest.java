package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Parentesco;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.service.pessoa.PessoaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PessoaController.class)
class PessoaControllerTest {

    private final String ENDPOINT = "/pessoas";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private PessoaService service;

    @MockBean
    private PessoaRepository repository;

    @DisplayName("Teste de cadastro de pessoa com dados válidos na API")
    @Test
    public void test_deve_criar_pessoa_se_dados_informados_validos() throws Exception {
        // Arrange
        when(repository.save(any(Pessoa.class))).thenReturn(
                new Pessoa(
                        1L,
                        "Fulano de tal",
                        LocalDate.of(1980, 1, 1),
                        Sexo.MASCULINO,
                        Parentesco.FILHO
                )
        );

        // Act
        this.mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"nome\": \"Fulano de Tal\", " +
                                        "\"dataNascimento\": \"1980-01-01\", " +
                                        "\"sexo\": \"MASCULINO\", " +
                                        "\"parentesco\": \"FILHO\"}"
                        )
        )
                // Asset
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(ENDPOINT + "/1")));
    }

}