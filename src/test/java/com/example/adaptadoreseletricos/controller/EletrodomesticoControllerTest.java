package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticoRepository;
import com.example.adaptadoreseletricos.service.eletrodomestico.EletrodomesticoService;
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


@WebMvcTest(EletrodomesticoController.class)
class EletrodomesticoControllerTest {

    private String endpoint = "/eletrodomesticos";

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
                        1L,
                        "Aparelho de som",
                        "XPTO",
                        "ABC",
                        200L
                )
        );

        // Act
        this.mockMvc.perform(
                        post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Aparelho de som\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marca\": \"ABC\", " +
                                                "\"potencia\": 200}"
                                )
                )
                // Assert
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("eletrodomesticos/1")));
    }

    @DisplayName("Teste de erro ao cadastrar eletrodoméstico com potência negativa")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_potencia_negativa() throws Exception{
        // Arrange/Act
        this.mockMvc.perform(
                post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"nome\": \"Aparelho de som\", " +
                                        "\"modelo\": \"XPTO\", " +
                                        "\"marca\": \"ABC\", " +
                                        "\"potencia\": -1}"
                        )
                )
                    // Assert
                        .andExpect(status().is4xxClientError());
    }

    @DisplayName("Teste de erro ao cadastrar eletrodoméstico com nome com mais de 120 caracteres")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_nome_muito_comprido() throws Exception{
        // Arrange/Act
        this.mockMvc.perform(
                        post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \" " + "a".repeat(121) + "\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marca\": \"ABC\", " +
                                                "\"potencia\": 1}"
                                )
                )
                // Assert
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("Teste de cadastro de eletrodomestico se modelo possui quantidade maxima de caracteres")
    @Test
    public void test_deve_criar_eletrodomestico_se_quantidade_maxima_de_modelo_eh_respeitada() throws Exception {
        // Arrange
        when(repository.save(any(Eletrodomestico.class))).thenReturn(
                new Eletrodomestico(
                        1L,
                        "b".repeat(120),
                        "XPTO",
                        "ABC",
                        200L
                )
        );

        // Act
        this.mockMvc.perform(
                        post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"" + "b".repeat(120) + "\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marca\": \"ABC\", " +
                                                "\"potencia\": 200}"
                                )
                )
                // Assert
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("eletrodomesticos/1")));
    }

}