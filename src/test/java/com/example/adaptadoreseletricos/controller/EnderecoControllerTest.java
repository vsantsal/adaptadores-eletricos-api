package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecosPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.service.endereco.EnderecoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class EnderecoControllerTest {

    private final String ENDPOINT = "/enderecos";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnderecoService service;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecosPessoasRepository enderecosPessoasRepository;

    private final Usuario usuario = new Usuario(
            "usuarioTester",
            "usuarioTester",
            new Pessoa(1L,
                    "Usuario Tester",
                    LocalDate.of(1981, 1, 1),
                    Sexo.FEMININO)
    );

    @BeforeEach
    public void setUp(){
        pessoaRepository.save(usuario.getPessoa());
    }

    @AfterEach
    public void tearDown(){
        enderecosPessoasRepository.deleteAll();
        enderecoRepository.deleteAll();
        pessoaRepository.deleteAll();
    }

    @DisplayName("Teste de cadastro de endereço válido na API")
    @Test
    public void test_deve_criar_endereco_se_dados_informados_validos() throws Exception {
        // Act
        this.mockMvc.perform(
                post(ENDPOINT)
                        .with(user(usuario))
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
                post(ENDPOINT)
                        .with(user(usuario))
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

    @DisplayName("Teste de detalhamento de endereço para id válido na API")
    @Test
    public void test_deve_detalhar_endereco_para_id_valido() throws Exception {
        // Arrange
        enderecoRepository.save(
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
        this.mockMvc.perform(get(ENDPOINT + "/1")
                .with(user(usuario)))
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
        // Act
        this.mockMvc.perform(get(ENDPOINT + "/1")
                        .with(user(usuario)))

            // Assert
            .andExpect(status().isNotFound());


    }

    @DisplayName("Teste de inclusão de endereço com rua muito comprida retorna erro")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_nome_rua_muito_comprida() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuario))
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
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_numero_negativo() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuario))
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

    @DisplayName("Teste de inclusão de endereço com sigla de estado muito comprido retorna erro")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_sigla_estado_muito_comprido() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuario))
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