package com.example.adaptadoreseletricos.controller;


import com.example.adaptadoreseletricos.domain.entity.pessoa.*;
import com.example.adaptadoreseletricos.domain.repository.pessoa.ParentescoPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.service.pessoa.PessoaService;
import org.junit.jupiter.api.*;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PessoaControllerTest {

    private final String ENDPOINT = "/pessoas";

    private final Usuario usuarioTesteMasculino = new Usuario(
            "usuarioTesteMasculino",
            "usuarioTesteMasculino",
            new Pessoa(3L,
                    "Usuario Teste Masculino",
                    LocalDate.of(1971, 1, 1),
                    Sexo.MASCULINO)
            );

    private final Usuario usuarioTesteFeminino = new Usuario(
            "usuarioTesteFeminino",
            "usuarioTesteFeminino",
            new Pessoa(2L,
                    "Usuario Teste Feminino",
                    LocalDate.of(1971, 1, 3),
                    Sexo.FEMININO)
    );

    private final Pessoa terceiraPessoa = new Pessoa(
            1L,
            "Ciclana de Só",
            LocalDate.of(1980, 1, 1),
            Sexo.FEMININO
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaService service;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ParentescoPessoasRepository parentescoPessoasRepository;

    @BeforeEach
    public void setUp(){
        pessoaRepository.save(terceiraPessoa);
        pessoaRepository.save(usuarioTesteFeminino.getPessoa());
        pessoaRepository.save(usuarioTesteMasculino.getPessoa());

    }

    @AfterEach
    public void tearDown(){
        parentescoPessoasRepository.deleteAll();
        pessoaRepository.deleteAll();
    }

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
    @Test
    public void test_deve_detalhar_pessoa_para_id_valido() throws Exception {
        // Arrange
        parentescoPessoasRepository.save(
          new ParentescoPessoas(
                  usuarioTesteMasculino.getPessoa(),
                  terceiraPessoa,
                  Parentesco.IRMA
          )
        );
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        terceiraPessoa,
                        usuarioTesteMasculino.getPessoa(),
                        Parentesco.IRMAO
                )
        );
        // Act
        this.mockMvc.perform(
                get(ENDPOINT +"/1")
                        .with(user(usuarioTesteMasculino))
                )
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

    @DisplayName("Teste de detalhamento de pessoa para id válido sem parentesco na API")
    @Test
    public void test_nao_deve_detalhar_pessoa_para_id_valido_sem_parentesco() throws Exception {
        // Act
        this.mockMvc.perform(
                get(ENDPOINT + "/2")
                        .with(user(usuarioTesteMasculino))
                )

                // Assert
                .andExpect(status().isNotFound());

    }

    @DisplayName("Teste de detalhamento de pessoa para Id inexistente na API")
    @Test
    public void test_nao_deve_detalhar_pessoa_para_id_invalido() throws Exception {
        // Act
        this.mockMvc.perform(
                get(ENDPOINT + "/1000")
                        .with(user(usuarioTesteFeminino))
                )

                // Assert
                .andExpect(status().isNotFound());

    }

    @DisplayName("Teste de cadastro de pessoa com dados válidos na API informando parentesco")
    @Test
    public void test_deve_criar_pessoa_se_dados_informados_validos_com_parentesco() throws Exception {
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
                .andExpect(header().exists("Location"));
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

    @DisplayName("Exclusão de pessoa retorna status 404 se id não existir")
    @Test
    public void test_exclusao_de_pessoa_que_nao_estah_na_base() throws Exception {
        // Act
        this.mockMvc.perform(
                delete(ENDPOINT + "/1000")
                        .with(user(usuarioTesteFeminino))
                )

                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Exclusão de pessoa retorna status 204 com id existente associada ao usuário")
    @Test
    public void test_exclusao_de_pessoa_que_estah_na_base() throws Exception {
        // Arrange
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        usuarioTesteFeminino.getPessoa(),
                        terceiraPessoa,
                        Parentesco.TIA
                )
        );
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        terceiraPessoa,
                        usuarioTesteFeminino.getPessoa(),
                        Parentesco.SOBRINHA
                )
        );
        // Act
        this.mockMvc.perform(
                        delete(ENDPOINT + "/1")
                                .with(user(usuarioTesteFeminino))

                )

                // Assert
                .andExpect(status().isNoContent());
    }

    @DisplayName("Exclusão de pessoa retorna status 404 com id existente não associada ao usuário")
    @Test
    public void test_exclusao_de_pessoa_que_estah_na_base_e_nao_tem_parentesco() throws Exception {
        // Act
        this.mockMvc.perform(
                        delete(ENDPOINT + "/1")
                                .with(user(usuarioTesteFeminino))

                )

                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve atualizar com sucesso todas as informações para pessoa com relacionamento válido")
    @Test
    public void test_atualizacao_valida() throws Exception {
        // Arrange
        parentescoPessoasRepository.save(
          new ParentescoPessoas(
                  usuarioTesteFeminino.getPessoa(),
                  terceiraPessoa,
                  Parentesco.IRMA
          )
        );
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        terceiraPessoa,
                        usuarioTesteFeminino.getPessoa(),
                        Parentesco.IRMA
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

    @DisplayName("Não pode atualizar dados para id existente mas não parente")
    @Test
    public void test_atualizacao_invalida_de_nao_parente() throws Exception {
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

    @DisplayName("Não pode atualizar dados para id inexistente")
    @Test
    public void test_atualizacao_invalida() throws Exception {
        // Act
        this.mockMvc.perform(
                        put( ENDPOINT + "/1000")
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

        //
        pessoaRepository.deleteById(1L);
        pessoaRepository.deleteById(3L);

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
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        usuarioTesteFeminino.getPessoa(),
                        usuarioTesteMasculino.getPessoa(),
                        Parentesco.PAI
                )
        );

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
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        usuarioTesteFeminino.getPessoa(),
                        usuarioTesteMasculino.getPessoa(),
                        Parentesco.PAI
                )
        );
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        usuarioTesteFeminino.getPessoa(),
                        terceiraPessoa,
                        Parentesco.MAE
                )
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuarioTesteFeminino))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(2)))
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
                .andExpect(jsonPath("$[1].id",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getId().intValue())))
                .andExpect(jsonPath("$[1].nome",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getNome())))
                .andExpect(jsonPath("$[1].dataNascimento",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getDataNascimento().toString())))
                .andExpect(jsonPath("$[1].sexo",
                        Matchers.is(usuarioTesteMasculino.getPessoa().getSexo().toString())))
                .andExpect(jsonPath("$[1].parentesco",
                        Matchers.is(Parentesco.PAI.name())))

        ;

    }

    @DisplayName("Listagem de parentes para repositório usuário, utros parentes e filtro parentesco")
    @Test
    public void test_listagem_de_parentes_para_repositorio_com_usuario_e_parentes_filtro_parentesco() throws Exception {
        // Arrange
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        usuarioTesteFeminino.getPessoa(),
                        usuarioTesteMasculino.getPessoa(),
                        Parentesco.PAI
                )
        );
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        usuarioTesteFeminino.getPessoa(),
                        terceiraPessoa,
                        Parentesco.MAE
                )
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT)
                                .param("parentesco", "PAI")
                                .with(user(usuarioTesteFeminino))
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

}