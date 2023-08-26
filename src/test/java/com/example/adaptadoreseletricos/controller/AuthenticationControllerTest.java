package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Sexo;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.UsuarioRepository;
import com.example.adaptadoreseletricos.service.pessoa.RegistroUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    private String endpointRegistro = "/auth/registrar";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegistroUsuarioService service;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PessoaRepository pessoaRepository;

    @DisplayName("Teste de registro para usuário registrado anteriormente retorna erro")
    @Test
    public void test_nao_deve_registrar_usuario_pela_segunda_vez() throws Exception {
        // Arrange
        when(usuarioRepository.findByLogin(any(String.class))).thenReturn(
                new Usuario()
        );

        // Act
        this.mockMvc.perform(
                post(endpointRegistro)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"login\": \"meulogin\", " +
                                        "\"senha\": \"minhasenha\", " +
                                        "\"pessoa\": {\"nome\": \"Meu nome\", " +
                                        "\"dataNascimento\": \"2001-12-31\", " +
                                        "\"sexo\": \"MASCULINO\"}}"

                        )
        )
            // Assert
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Teste de registro para novo usuário")
    @Test
    public void test_deve_registrar_usuario_pela_primeira_vez() throws Exception {
        // Arrange
        when(usuarioRepository.findByLogin(any(String.class))).thenReturn(
                null
        );
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(
                new Pessoa(
                        1L,
                        "Meu nome",
                        LocalDate.of(2001, 12, 31),
                        Sexo.MASCULINO
                )
        );

        // Act
        this.mockMvc.perform(
                post(endpointRegistro)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"login\": \"meulogin\", " +
                                        "\"senha\": \"minhasenha\", " +
                                        "\"pessoa\": {\"nome\": \"Meu nome\", " +
                                        "\"dataNascimento\": \"2001-12-31\", " +
                                        "\"sexo\": \"MASCULINO\"}}"

                        )
        )
            // Assert
                .andExpect(status().isOk());

    }

}