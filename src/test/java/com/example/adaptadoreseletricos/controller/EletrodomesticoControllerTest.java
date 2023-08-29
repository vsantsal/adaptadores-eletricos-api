package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoas;
import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoasChave;
import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoas;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticoRepository;
import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticosPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecosPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.service.eletrodomestico.EletrodomesticoService;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class EletrodomesticoControllerTest {

    private String ENDPOINT = "/eletrodomesticos";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EletrodomesticoService service;

    @Autowired
    private EletrodomesticoRepository eletrodomesticoRepository;

    @Autowired
    private EletrodomesticosPessoasRepository eletrodomesticosPessoasRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecosPessoasRepository enderecosPessoasRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    private final Usuario usuario = new Usuario(
            "usuarioTester",
            "usuarioTester",
            new Pessoa(1L,
                    "Usuario Tester",
                    LocalDate.of(1961, 12, 31),
                    Sexo.MASCULINO)
    );

    private  final Endereco enderecoPadrao = new Endereco(
            1L,
            "Rua Nascimento Silva",
            107L,
            "Ipanema",
            "Rio de Janeiro",
            Estado.RJ);

    private final Eletrodomestico eletrodomesticoPadrao = new Eletrodomestico(
            1L,
            "Aparelho de som",
            "XPTO",
            "ABC",
            200L,
            enderecoPadrao
    );

    @BeforeEach
    public void setUp(){
        pessoaRepository.save(usuario.getPessoa());
        enderecoRepository.save(enderecoPadrao);
    }

    @AfterEach
    public void tearDown(){
        eletrodomesticosPessoasRepository.deleteAll();
        enderecosPessoasRepository.deleteAll();
        eletrodomesticoRepository.deleteAll();
        enderecoRepository.deleteAll();
        pessoaRepository.deleteAll();
    }

    @DisplayName("Teste de cadastro de eletrodomestico válido na API")
    @Test
    public void test_deve_criar_eletrodomestico_se_dados_informados_validos() throws Exception {
        // Arrange
        enderecoRepository.save(enderecoPadrao);
        // Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuario))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Aparelho de som\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marca\": \"ABC\", " +
                                                "\"idEndereco\": 1, " +
                                                "\"potencia\": 200}"
                                )
                )
                // Assert
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("eletrodomesticos/1")));
    }

    @DisplayName("Teste de associação de eletrodomestico cadastrado com usuario logado")
    @Test
    public void test_deve_associar_eletrodomestico_a_usuario_logado() throws Exception {
        // Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuario))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Aparelho de som\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marca\": \"ABC\", " +
                                                "\"idEndereco\": 1, " +
                                                "\"potencia\": 200}"
                                )
                )
                // Assert
                .andExpect(status().isCreated());

        Eletrodomestico eletrodomestico = eletrodomesticoRepository.getReferenceById(1L);
        boolean houveAssociacao = eletrodomesticosPessoasRepository.existsById(
                new EletrodomesticosPessoasChave(
                        usuario.getPessoa(),
                        eletrodomestico)
        );
        assertTrue(houveAssociacao);
    }

    @DisplayName("Teste de erro ao cadastrar eletrodoméstico com potência negativa")
    @Test
    public void test_deve_informar_erro_requisicao_cliente_se_potencia_negativa() throws Exception{
        // Arrange/Act
        this.mockMvc.perform(
                post(ENDPOINT)
                        .with(user(usuario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"nome\": \"Aparelho de som\", " +
                                        "\"modelo\": \"XPTO\", " +
                                        "\"marca\": \"ABC\", " +
                                        "\"idEndereco\": 1, " +
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
                        post(ENDPOINT)
                                .with(user(usuario))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \" " + "a".repeat(121) + "\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marca\": \"ABC\", " +
                                                "\"idEndereco\": 1, " +
                                                "\"potencia\": 1}"
                                )
                )
                // Assert
                .andExpect(status().is4xxClientError());
    }


    @DisplayName("Teste de cadastro de eletrodomestico se modelo possui quantidade maxima de caracteres")
    @Test
    public void test_deve_criar_eletrodomestico_se_quantidade_maxima_de_modelo_eh_respeitada() throws Exception {
        // Act
        this.mockMvc.perform(
                        post(ENDPOINT)
                                .with(user(usuario))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"" + "b".repeat(120) + "\", " +
                                                "\"modelo\": \"XPTO\", " +
                                                "\"marca\": \"ABC\", " +
                                                "\"idEndereco\": 1, " +
                                                "\"potencia\": 200}"
                                )
                )
                // Assert
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("eletrodomesticos/1")));
    }

    @DisplayName("Teste de detalhamento de eletrodoméstico para id válido na API")
    @Test
    public void test_deve_detalhar_eletrodomestico_para_id_valido() throws Exception {
        // Arrange
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        // Act
        this.mockMvc.perform(get(ENDPOINT + "/1")
                .with(user(usuario)))
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
    @Test
    public void test_nao_deve_detalhar_eletrodomestico_para_id_invalido() throws Exception {
        // Act
        this.mockMvc.perform(get(ENDPOINT + "/1")
                .with(user(usuario)))

                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Teste de detalhamento de eletrodoméstico para id existente mas não associado na API")
    @Test
    public void test_nao_deve_detalhar_eletrodomestico_para_id_existente_nao_associado() throws Exception {
        // Arrange
        eletrodomesticoRepository.save(
                new Eletrodomestico(
                        2L,
                        "DVD Player",
                        "XYZ",
                        "DEF",
                        100L,
                        enderecoPadrao
                )
        );

        // Act
        this.mockMvc.perform(get(ENDPOINT + "/2")
                        .with(user(usuario)))

                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Teste de remoção de eletrodoméstico associado ao usuário retorna status 204")
    @Test
    public void test_remocao_de_eletrodomestico_associado_ao_usuario_retorna_status_204() throws Exception {
        // Arrange
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(),
                        eletrodomesticoPadrao));

        // Act
        this.mockMvc.perform(
                        delete(ENDPOINT + "/1")
                                .with(user(usuario))
                )

                // Assert
                .andExpect(status().isNoContent());
    }

    @DisplayName("Teste de remoção de eletrodoméstico associado ao usuário desativa a relação")
    @Test
    public void test_remocao_de_eletrodomestico_associado_ao_usuario_apaga_registros_pertinentes() throws Exception {
        // Arrange
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(),
                        eletrodomesticoPadrao));

        // Act
        this.mockMvc.perform(
                delete(ENDPOINT + "/1")
                        .with(user(usuario))
        );

        // Assert
        var associacaoAtualizada = eletrodomesticosPessoasRepository
                .findAll().stream().findFirst().get();
        assertFalse(associacaoAtualizada.isAtivo());

    }

    @DisplayName("Teste de remoção de eletrodoméstico não associado ao usuário retorna status 404")
    @Test
    public void test_remocao_de_eletrodomestico_associado_ao_usuario_retorna_status_404() throws Exception {
        // Arrange
        eletrodomesticoRepository.save(eletrodomesticoPadrao);

        // Act
        this.mockMvc.perform(
                        delete(ENDPOINT + "/1")
                                .with(user(usuario))
                )

                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve atualizar informações para eletrodoméstico associado ao usuário")
    @Test
    public void test_atualizacao_valida_para_eletrodomestico_associado()  throws Exception {
        // Arrange
        enderecosPessoasRepository.save(
                new EnderecosPessoas(usuario.getPessoa(), enderecoPadrao)
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );


        // Act
        this.mockMvc.perform(
                        put( ENDPOINT + "/1")
                                .with(user(usuario))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"DVD Player\", " +
                                                "\"modelo\": \"XYZ\", " +
                                                "\"marca\": \"DEF\", " +
                                                "\"idEndereco\": 1, " +
                                                "\"potencia\": 110}"
                                )
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$.nome",
                        Matchers.is("DVD Player")))
                .andExpect(jsonPath("$.modelo",
                        Matchers.is("XYZ")))
                .andExpect(jsonPath("$.marca",
                        Matchers.is("DEF")))
                .andExpect(jsonPath("$.idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$.potencia",
                        Matchers.is(110)))
        ;
    }

    @DisplayName("Não pode atualizar informações para eletrodoméstico não associado ao usuário")
    @Test
    public void test_nao_pode_atualizar_eletrodomestico_nao_associado_ao_usuario() throws Exception {
        // Arrange
        eletrodomesticoRepository.save(eletrodomesticoPadrao);

        // Act
        this.mockMvc.perform(
                put( ENDPOINT + "/1")
                        .with(user(usuario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"nome\": \"DVD Player\", " +
                                        "\"modelo\": \"XYZ\", " +
                                        "\"marca\": \"DEF\", " +
                                        "\"idEndereco\": 1, " +
                                        "\"potencia\": 110}"
                        )
        )
                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Não pode atualizar informações para eletrodoméstico associado ao usuário mas inativo")
    @Test
    public void test_atualizacao_invalida_eletrodomestico_inativo() throws Exception {
        // Arrange
        var associacao = new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao);
        associacao.desativar();
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticosPessoasRepository.save(associacao);

        // Act
        this.mockMvc.perform(
                        put( ENDPOINT + "/1")
                                .with(user(usuario))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"DVD Player\", " +
                                                "\"modelo\": \"XYZ\", " +
                                                "\"marca\": \"DEF\", " +
                                                "\"idEndereco\": 1, " +
                                                "\"potencia\": 110}"
                                )
                )
                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Não pode atualizar informações para eletrodoméstico com endereço não associado ao usuário")
    @Test
    public void test_nao_pode_atualizar_eletrodomestico_com_endereco_nao_associado_ao_usuario() throws Exception {
        // Arrange
        Endereco outroEndereco = new Endereco(
                2L,
                "Avenida Lins de Vasconcelos",
                1264L,
                "Cambuci",
                "São Paulo",
                Estado.SP
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        enderecoRepository.save(outroEndereco);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );

        // Act
        this.mockMvc.perform(
                        put( ENDPOINT + "/1")
                                .with(user(usuario))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"DVD Player\", " +
                                                "\"modelo\": \"XYZ\", " +
                                                "\"marca\": \"DEF\", " +
                                                "\"idEndereco\": 2, " +
                                                "\"potencia\": 110}"
                                )
                )
                // Assert
                .andExpect(status().isNotFound());
    }

    @DisplayName("Listagem de eletrodomésticos para repositório vazio")
    @Test
    public void test_listagem_de_eletrodomesticos_para_repositorio_vazio() throws Exception {
        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(0)));
    }

    @DisplayName("Listagem de eletrodomésticos para repositório sem eletro associado ao usuário logado")
    @Test
    public void test_listagem_de_eletrodomesticos_para_repositorio_sem_eletro_associado_ao_usuario_logado() throws Exception {
        // Arrange
        Pessoa outraPessoa = new Pessoa(
                2L,
                "Usuario Testado",
                LocalDate.of(1961, 2, 28),
                Sexo.MASCULINO
        );
        pessoaRepository.save(outraPessoa);
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(outraPessoa, eletrodomesticoPadrao)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(0)));
    }

    @DisplayName("Listagem de eletrodomésticos para repositório com unico eletro associado ao usuário logado")
    @Test
    public void test_listagem_de_eletros_para_repositorio_com_eletro_associado_ao_usuario_logado() throws Exception {
        // Arrange
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(200)))
        ;
    }

    @DisplayName("Listagem de eletrodomesticos para repositório com mais eletros associados ao usuário logado")
    @Test
    public void test_listagem_de_eletros_para_repositorio_com_mais_eletros_associados_ao_usuario_logado() throws Exception {
        // Arrange
        Eletrodomestico outroEletro = new Eletrodomestico(
                2L,
                "DVD Player",
                "XYZ",
                "DEF",
                110L,
                enderecoPadrao
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticoRepository.save(outroEletro);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), outroEletro)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(200)))
                .andExpect(jsonPath("$[1].id",
                        Matchers.is(2)))
                .andExpect(jsonPath("$[1].nome",
                        Matchers.is("DVD Player")))
                .andExpect(jsonPath("$[1].modelo",
                        Matchers.is("XYZ")))
                .andExpect(jsonPath("$[1].marca",
                        Matchers.is("DEF")))
                .andExpect(jsonPath("$[1].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[1].potencia",
                        Matchers.is(110)))
        ;
    }

    @DisplayName("Listagem de eletrodomesticos com mais eletros associados não apresenta endereço inativo")
    @Test
    public void test_listagem_de_eletros_com_mais_eletros_nao_apresenta_inativo() throws Exception {
        // Arrange
        Eletrodomestico outroEletro = new Eletrodomestico(
                2L,
                "DVD Player",
                "XYZ",
                "DEF",
                110L,
                enderecoPadrao
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticoRepository.save(outroEletro);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );
        var associacaoInativa = new EletrodomesticosPessoas(usuario.getPessoa(), outroEletro);
        associacaoInativa.desativar();
        eletrodomesticosPessoasRepository.save(
                associacaoInativa
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(200)))
        ;
    }

    @DisplayName("Listagem de eletrodomesticos com filtro de nome")
    @Test
    public void test_listagem_de_eletros_com_filtro_de_nome() throws Exception {
        // Arrange
        Eletrodomestico outroEletro = new Eletrodomestico(
                2L,
                "DVD Player",
                "XYZ",
                "DEF",
                110L,
                enderecoPadrao
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticoRepository.save(outroEletro);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), outroEletro)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                                .param("nome", eletrodomesticoPadrao.getNome())
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(200)))

        ;
    }

    @DisplayName("Listagem de eletrodomesticos com filtro de modelo")
    @Test
    public void test_listagem_de_eletros_com_filtro_de_modelo() throws Exception {
        // Arrange
        Eletrodomestico outroEletro = new Eletrodomestico(
                2L,
                "DVD Player",
                "XYZ",
                "DEF",
                110L,
                enderecoPadrao
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticoRepository.save(outroEletro);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), outroEletro)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                                .param("modelo", "XYZ")
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(2)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("DVD Player")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XYZ")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("DEF")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(110)))
        ;
    }

    @DisplayName("Listagem de eletrodomesticos com filtro de marca")
    @Test
    public void test_listagem_de_eletros_com_filtro_de_marca() throws Exception {
        // Arrange
        Eletrodomestico outroEletro = new Eletrodomestico(
                2L,
                "DVD Player",
                "XYZ",
                "DEF",
                110L,
                enderecoPadrao
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticoRepository.save(outroEletro);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), outroEletro)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                                .param("marca", eletrodomesticoPadrao.getMarca())
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(200)))

        ;
    }

    @DisplayName("Listagem de eletrodomesticos com filtro de endereco")
    @Test
    public void test_listagem_de_eletros_com_filtro_de_endereco() throws Exception {
        // Arrange
        Eletrodomestico outroEletro = new Eletrodomestico(
                2L,
                "DVD Player",
                "XYZ",
                "DEF",
                110L,
                enderecoPadrao
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticoRepository.save(outroEletro);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), outroEletro)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                                .param("idEndereco", "1")
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(200)))
                .andExpect(jsonPath("$[1].id",
                        Matchers.is(2)))
                .andExpect(jsonPath("$[1].nome",
                        Matchers.is("DVD Player")))
                .andExpect(jsonPath("$[1].modelo",
                        Matchers.is("XYZ")))
                .andExpect(jsonPath("$[1].marca",
                        Matchers.is("DEF")))
                .andExpect(jsonPath("$[1].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[1].potencia",
                        Matchers.is(110)))
        ;
    }

    @DisplayName("Listagem de eletrodomesticos com filtro de potencia")
    @Test
    public void test_listagem_de_eletros_com_filtro_de_potencia() throws Exception {
        // Arrange
        Eletrodomestico outroEletro = new Eletrodomestico(
                2L,
                "DVD Player",
                "XYZ",
                "DEF",
                110L,
                enderecoPadrao
        );
        eletrodomesticoRepository.save(eletrodomesticoPadrao);
        eletrodomesticoRepository.save(outroEletro);
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), eletrodomesticoPadrao)
        );
        eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(usuario.getPessoa(), outroEletro)
        );

        // Act
        this.mockMvc.perform(
                        get(ENDPOINT).with(user(usuario))
                                .param("potencia",
                                        eletrodomesticoPadrao.getPotencia().toString())
                )
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        Matchers.hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].nome",
                        Matchers.is("Aparelho de som")))
                .andExpect(jsonPath("$[0].modelo",
                        Matchers.is("XPTO")))
                .andExpect(jsonPath("$[0].marca",
                        Matchers.is("ABC")))
                .andExpect(jsonPath("$[0].idEndereco",
                        Matchers.is(1)))
                .andExpect(jsonPath("$[0].potencia",
                        Matchers.is(200)))

        ;
    }

}