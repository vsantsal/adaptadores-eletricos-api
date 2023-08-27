package com.example.adaptadoreseletricos.domain.repository.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class ParentescoPessoasRepositoryTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ParentescoPessoasRepository parentescoPessoasRepository;

    @Autowired
    private  PessoaRepository pessoaRepository;

    @BeforeEach
    public void setUp(){

        pessoaRepository.save(new Pessoa(1L, "1", LocalDate.now(), Sexo.FEMININO));
        pessoaRepository.save(new Pessoa(2L, "2", LocalDate.now(), Sexo.MASCULINO));
        pessoaRepository.save(new Pessoa(3L, "3", LocalDate.now(), Sexo.FEMININO));
        pessoaRepository.save(new Pessoa(4L, "4", LocalDate.now(), Sexo.MASCULINO));
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        new Pessoa(1L, "1", LocalDate.now(), Sexo.FEMININO),
                        new Pessoa(2L, "2", LocalDate.now(), Sexo.MASCULINO),
                        Parentesco.IRMAO
                )
        );
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        new Pessoa(2L, "2", LocalDate.now(), Sexo.MASCULINO),
                        new Pessoa(1L, "1", LocalDate.now(), Sexo.FEMININO),
                        Parentesco.IRMA
                )
        );
        parentescoPessoasRepository.save(
                new ParentescoPessoas(
                        new Pessoa(2L, "2", LocalDate.now(), Sexo.MASCULINO),
                        new Pessoa(3L, "3", LocalDate.now(), Sexo.FEMININO),
                        Parentesco.IRMA
                )
        );
    }

    @AfterEach
    public void tearDown(){
        parentescoPessoasRepository.deleteAll();
        pessoaRepository.deleteAll();
    }

    @DisplayName("Teste de remoção de todos parentescos de id com parentesco")
    @Test
    public void test_remocao_de_todos_parentescos_para_id_com_parentesco(){
        // Act
        parentescoPessoasRepository.removerTodosParentescosDePessoa(1L);

        // Assert
        assertEquals(1, parentescoPessoasRepository.count());

    }

    @DisplayName("Teste de remoção de todos parentescos para id sem parentesco")
    @Test
    public void test_remocao_de_todos_parentescos_para_id_sem_parentesco(){
        // Act
        parentescoPessoasRepository.removerTodosParentescosDePessoa(4L);

        // Assert
        assertEquals(3, parentescoPessoasRepository.count());

    }

    @DisplayName("Teste de remoção de todos parentescos para id inexistente")
    @Test
    public void test_remocao_de_todos_parentescos_para_id_inexistente(){
        // Act
        parentescoPessoasRepository.removerTodosParentescosDePessoa(5L);

        // Assert
        assertEquals(3, parentescoPessoasRepository.count());

    }

    @DisplayName("Teste de remoção de todos parentescos quando não há parentesco")
    @Test
    public void test_remocao_de_todos_parentescos_para_id_quando_nao_ha_nenhum_parentesco(){
        // Arrange
        parentescoPessoasRepository.deleteAll();

        // Act
        parentescoPessoasRepository.removerTodosParentescosDePessoa(4L);

        // Assert
        assertEquals(0, parentescoPessoasRepository.count());

    }

    @DisplayName("Teste de atualização de parentesco existente")
    @Test
    public void test_atualizacao_de_parentesco_existente(){
        // Act
        parentescoPessoasRepository.atualizarParentescoEntrePessoas(
                1L, 2L, Parentesco.SOBRINHO);

        // Assert
        assertEquals(3, parentescoPessoasRepository.count());
        Parentesco parentescoEncontrado = parentescoPessoasRepository
                .obterParentescoParaPessoas(1L, 2L);
        assertEquals(Parentesco.SOBRINHO, parentescoEncontrado);

    }

    @DisplayName("Teste de atualização de parentesco inexistente")
    @Test
    public void test_atualizacao_de_parentesco_inexistente(){
        // Act
        parentescoPessoasRepository.atualizarParentescoEntrePessoas(
                1L, 3L, Parentesco.SOBRINHO);

        // Assert
        assertEquals(3, parentescoPessoasRepository.count());
        Parentesco parentescoEncontrado = parentescoPessoasRepository
                .obterParentescoParaPessoas(1L, 3L);
        assertNull(parentescoEncontrado);

    }

}