package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticoRepository;
import com.example.adaptadoreseletricos.service.eletrodomestico.EletrodomesticoService;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
class EletrodomesticoControllerTest {

    private String endpoint = "/eletrodomesticos";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private EletrodomesticoService service;

    @MockBean
    private EletrodomesticoRepository repository;

    @DisplayName("Teste de cadastro de eletrodomestico válido na API")
    @WithMockUser(username = "tester")
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
                                .with(csrf())
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
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_potencia_negativa() throws Exception{
        // Arrange/Act
        this.mockMvc.perform(
                post(endpoint)
                        .with(csrf())
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
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_nome_muito_comprido() throws Exception{
        // Arrange/Act
        this.mockMvc.perform(
                        post(endpoint)
                                .with(csrf())
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
    @WithMockUser(username = "tester")
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
                                .with(csrf())
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

    @DisplayName("Teste de detalhamento de eletrodoméstico para id válido na API")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_detalhar_eletrodomestico_para_id_valido() throws Exception {
        // Arrange
        when(repository.getReferenceById(1L)).thenReturn(
                new Eletrodomestico(
                        1L,
                        "Aparelho de som",
                        "XPTO",
                        "ABC",
                        200L
                )
        );
        // Act
        this.mockMvc.perform(get(endpoint + "/1")
                .with(csrf()))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$.nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$.modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$.marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$.potencia",
                        Matchers.is(200)));

    }

    @DisplayName("Teste de detalhamento de eletrodoméstico para id inexistente na API")
    @WithMockUser(username = "tester")
    @Test
    public void test_nao_deve_detalhar_eletrodomestico_para_id_invalido() throws Exception {
        // Arrange
        when(repository.getReferenceById(2L)).thenThrow(
                EntityNotFoundException.class
        );

        // Act
        this.mockMvc.perform(get(endpoint + "/2")
                        .with(csrf()))

                // Assert
                .andExpect(status().isNotFound());
    }

}