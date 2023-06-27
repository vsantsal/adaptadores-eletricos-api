package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class EletrodomesticoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private EletrodomesticoService service;

    @MockBean
    private EletrodomesticoRepository repository;

    @DisplayName("Teste de cadastro de eletrodomestico válido na API")
    @Test
    public void test_deve_criar_eletrodomestico_se_dados_informados_validos() throws Exception {
        // Arrange
        when(repository.save(any(Eletrodomestico.class))).thenReturn(
                new Eletrodomestico(
                        "Aparelho de som",
                        "XPTO",
                        "ABC",
                        200
                )
        );

        // Act
        this.mockMvc.perform(
                        post("/eletrodomesticos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Aparelho de som\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marc\": \"ABC\", " +
                                                "\"potencia\": 200}"
                                )
                )
                // Assert
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("eletrodomesticos/1")));
    }

}