package com.example.adaptadoreseletricos.controller;


import com.example.adaptadoreseletricos.domain.entity.pessoa.Parentesco;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import com.example.adaptadoreseletricos.domain.repository.pessoa.ParentescoPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.service.pessoa.PessoaService;
import org.junit.jupiter.api.Disabled;
import org.springframework.data.domain.Example;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PessoaControllerTest {

    private final String ENDPOINT = "/pessoas";

    private final Usuario usuarioTesteMasculino = new Usuario(
            "usuarioTesteMasculino",
            "usuarioTesteMasculino",
            new Pessoa(42L,
                    "Usuario Teste Masculino",
                    LocalDate.of(1971, 1, 1),
                    Sexo.MASCULINO)
            );

    private final Usuario usuarioTesteFeminino = new Usuario(
            "usuarioTesteFeminino",
            "usuarioTesteFeminino",
            new Pessoa(43L,
                    "Usuario Teste Feminino",
                    LocalDate.of(1971, 1, 3),
                    Sexo.FEMININO)
    );

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private PessoaService service;

    @MockBean
    private PessoaRepository pessoaRepository;

    @MockBean
    private ParentescoPessoasRepository parentescoPessoasRepository;

    @DisplayName("Teste de cadastro de pessoa com sexo inválido")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_sexo_invalido() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                post(ENDPOINT)
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
        when(pessoaRepository.getReferenceById(1L)).thenReturn(
                new Pessoa(
                        1L,
                        "Ciclana de Só",
                        LocalDate.of(1980, 1, 1),
                        Sexo.FEMININO
                )
        );

        // Act
        this.mockMvc.perform(get(ENDPOINT +"/1"))
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
        when(pessoaRepository.getReferenceById(2L)).thenThrow(
                EntityNotFoundException.class
        );

        // Act
        this.mockMvc.perform(get(ENDPOINT + "/2"))

                // Assert
                .andExpect(status().isNotFound());

    }

    @DisplayName("Teste com erro de integridade de dados no BD")
    @WithMockUser(username = "tester")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_provoca_erro_integridade_dados() throws Exception {
        // Arrange
        when(pessoaRepository.save(any(Pessoa.class))).thenThrow(
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

    @DisplayName("Teste de cadastro de pessoa com dados válidos na API informando parentesco")
    @Test
    public void test_deve_criar_pessoa_se_dados_informados_validos_com_parentesco() throws Exception {
        // Arrange
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(
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
                                .with(user(usuarioTesteMasculino))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"" + "F".repeat(120) + "\", " +
                                                "\"dataNascimento\": \"1991-01-01\", " +
                                                "\"sexo\": \"MASCULINO\", " +
                                                "\"parentesco\": \"FILHO\"}"
                                )
                )
                // Asset
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(ENDPOINT + "/1")));
    }

    @DisplayName("Teste usuário logado não pode se cadastrar pelo endpoint de gestão de pessoas")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_usuario_tenta_se_cadastrar_de_novo() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuarioTesteFeminino))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"" +
                                                usuarioTesteFeminino.getPessoa().getNome()
                                                + "\", " +
                                                "\"dataNascimento\": " +
                                                usuarioTesteFeminino.getPessoa().getDataNascimento()
                                                + "\", " +
                                                "\"sexo\": " +
                                                usuarioTesteFeminino.getPessoa().getSexo()
                                                + "}"
                                )
                )
                // Assert
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Teste aplicação valida coerência entre sexo e parentesco informados")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_parentesco_e_sexo_incoerentes() throws Exception {
        // Arrange/Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuarioTesteFeminino))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"" + "F".repeat(120) + "\", " +
                                                "\"dataNascimento\": \"1991-01-01\", " +
                                                "\"sexo\": \"MASCULINO\", " +
                                                "\"parentesco\": \"FILHA\"}"
                                )
                )
                // Assert
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Exclusão de pessoa retorna status 204 mesmo se id não existir")
    @WithMockUser(username = "tester")
    @Test
    public void test_exclusao_de_pessoa_que_nao_estah_na_base() throws Exception {
        // Act
        this.mockMvc.perform(
                delete(ENDPOINT + "/1")

                )

                // Assert
                .andExpect(status().isNoContent());
    }

    @DisplayName("Exclusão de pessoa retorna status 204 com id existente")
    @WithMockUser(username = "tester")
    @Test
    public void test_exclusao_de_pessoa_que_estah_na_base() throws Exception {
        // Arrange
        when(pessoaRepository.getReferenceById(1L)).thenReturn(
                new Pessoa(
                        43L,
                        "Ciclana de Só",
                        LocalDate.of(1980, 1, 1),
                        Sexo.FEMININO
                )
        );
        // Act
        this.mockMvc.perform(
                        delete(ENDPOINT + "/43")

                )

                // Assert
                .andExpect(status().isNoContent());
    }

    @DisplayName("Deve atualizar com sucesso todas as informações para pessoa com relacionamento válido")
    @Test
    public void test_atualizacao_valida() throws Exception {
        // Arrange
        when(pessoaRepository.getReferenceById(1L)).thenReturn(
                new Pessoa(
                        1L,
                        "F".repeat(120),
                        LocalDate.of(2001, 1, 1),
                        Sexo.MASCULINO
                )
        );

        // Act
        this.mockMvc.perform(
                        put( ENDPOINT + "/1")
                                .with(user(usuarioTesteFeminino))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Fulana\", " +
                                                "\"dataNascimento\": \"2001-01-02\", " +
                                                "\"parentesco\": \"FILHA\", " +
                                                "\"sexo\": \"FEMININO\"}"
                                )
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$.nome",
                        Matchers.is("Fulana")))
                .andExpect(jsonPath("$.dataNascimento",
                        Matchers.is("2001-01-02")))
                .andExpect(jsonPath("$.parentesco",
                        Matchers.is("FILHA")))
                .andExpect(jsonPath("$.sexo",
                        Matchers.is("FEMININO")))
        ;
    }

    @DisplayName("Não pode atualizar dados para id inexistente")
    @Test
    public void test_atualizacao_invalida() throws Exception {
        // Arrange
        when(pessoaRepository.getReferenceById(1L)).thenThrow(
                EntityNotFoundException.class
        );

        // Act
        this.mockMvc.perform(
                        put( ENDPOINT + "/1")
                                .with(user(usuarioTesteMasculino))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Fulana\", " +
                                                "\"dataNascimento\": \"2001-01-02\", " +
                                                "\"parentesco\": \"FILHA\", " +
                                                "\"sexo\": \"FEMININO\"}"
                                ))
                // Assert
                .andExpect(status().isNotFound())
        ;
    }

    @DisplayName("Listagem de parentes para repositório com apenas próprio usuário")
    @Test
    public void test_listagem_de_parentes_para_repositorio_soh_com_usuario() throws Exception {
        // Arrange
        when(pessoaRepository.findAll(ArgumentMatchers.isA(Example.class))).thenReturn(
                List.of(usuarioTesteFeminino.getPessoa())
        );

        // Act
        this.mockMvc.perform(
                get(ENDPOINT).with(user(usuarioTesteFeminino))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(0)));
    }

    @DisplayName("Listagem de parentes para repositório usuário e outro não parente")
    @Test
    public void test_listagem_de_parentes_para_repositorio_com_usuario_e_nao_parente() throws Exception {
        // Arrange
        when(pessoaRepository.findAll(ArgumentMatchers.isA(Example.class))).thenReturn(
                List.of(
                        usuarioTesteFeminino.getPessoa(),
                        usuarioTesteMasculino.getPessoa()
                )
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuarioTesteFeminino))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(0)));
    }

    @DisplayName("Listagem de parentes para repositório usuário e outro parente")
    @Test
    public void test_listagem_de_parentes_para_repositorio_com_usuario_e_parente() throws Exception {
        // Arrange
        when(pessoaRepository.findAll(ArgumentMatchers.isA(Example.class))).thenReturn(
                List.of(
                        usuarioTesteFeminino.getPessoa(),
                        usuarioTesteMasculino.getPessoa()
                )
        );
        when(parentescoPessoasRepository.obterParentescoParaPessoas(
                usuarioTesteFeminino.getPessoa().getId(),
                usuarioTesteMasculino.getPessoa().getId()
        )).thenReturn(Parentesco.PAI);

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuarioTesteFeminino))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getId().intValue())))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getNome())))
                .andExpect(jsonPath("$[0].dataNascimento",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getDataNascimento().toString())))
                .andExpect(jsonPath("$[0].sexo",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getSexo().toString())))
                .andExpect(jsonPath("$[0].parentesco",
                        Matchers.is(Parentesco.PAI.name())))
        ;

    }

    @DisplayName("Listagem de parentes para repositório usuário e outros parentes")
    @Test
    public void test_listagem_de_parentes_para_repositorio_com_usuario_e_parentes() throws Exception {
        // Arrange
        Pessoa terceiraPessoa = new Pessoa(44L, "44", LocalDate.now(), Sexo.FEMININO);
        when(pessoaRepository.findAll(ArgumentMatchers.isA(Example.class))).thenReturn(
                List.of(
                        usuarioTesteFeminino.getPessoa(),
                        usuarioTesteMasculino.getPessoa(),
                        terceiraPessoa
                )
        );
        when(parentescoPessoasRepository.obterParentescoParaPessoas(
                usuarioTesteFeminino.getPessoa().getId(),
                usuarioTesteMasculino.getPessoa().getId()
        )).thenReturn(Parentesco.PAI);
        when(parentescoPessoasRepository.obterParentescoParaPessoas(
                usuarioTesteFeminino.getPessoa().getId(),
                terceiraPessoa.getId()
        )).thenReturn(Parentesco.MAE);

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuarioTesteFeminino))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getId().intValue())))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getNome())))
                .andExpect(jsonPath("$[0].dataNascimento",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getDataNascimento().toString())))
                .andExpect(jsonPath("$[0].sexo",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getSexo().toString())))
                .andExpect(jsonPath("$[0].parentesco",
                        Matchers.is(Parentesco.PAI.name())))
                .andExpect(jsonPath("$[1].id",
                        Matchers.is(terceiraPessoa.getId().intValue())))
                .andExpect(jsonPath("$[1].nome",
                        Matchers.is(terceiraPessoa.getNome())))
                .andExpect(jsonPath("$[1].dataNascimento",
                        Matchers.is(terceiraPessoa.getDataNascimento().toString())))
                .andExpect(jsonPath("$[1].sexo",
                        Matchers.is(terceiraPessoa.getSexo().toString())))
                .andExpect(jsonPath("$[1].parentesco",
                        Matchers.is(Parentesco.MAE.name())))
        ;

    }

    @Disabled("WIP: Avaliando falha do teste")
    @DisplayName("Listagem de parentes para repositório usuário, utros parentes e filtro nome")
    @Test
    public void test_listagem_de_parentes_para_repositorio_com_usuario_e_parentes_filtro_nome() throws Exception {
        // Arrange
        Pessoa terceiraPessoa = new Pessoa(44L, "44", LocalDate.now(), Sexo.FEMININO);
        when(pessoaRepository.findAll(ArgumentMatchers.isA(Example.class))).thenReturn(
                List.of(
                        usuarioTesteFeminino.getPessoa(),
                        usuarioTesteMasculino.getPessoa(),
                        terceiraPessoa
                )
        );
        when(parentescoPessoasRepository.obterParentescoParaPessoas(
                usuarioTesteFeminino.getPessoa().getId(),
                usuarioTesteMasculino.getPessoa().getId()
        )).thenReturn(Parentesco.PAI);
        when(parentescoPessoasRepository.obterParentescoParaPessoas(
                usuarioTesteFeminino.getPessoa().getId(),
                terceiraPessoa.getId()
        )).thenReturn(Parentesco.MAE);

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT)
                                .param("nome", "44")
                                .with(user(usuarioTesteFeminino))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(terceiraPessoa.getId().intValue())))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is(terceiraPessoa.getNome())))
                .andExpect(jsonPath("$[0].dataNascimento",
                        Matchers.is(terceiraPessoa.getDataNascimento().toString())))
                .andExpect(jsonPath("$[0].sexo",
                        Matchers.is(terceiraPessoa.getSexo().toString())))
                .andExpect(jsonPath("$[0].parentesco",
                        Matchers.is(Parentesco.MAE.name())))
        ;

    }

}