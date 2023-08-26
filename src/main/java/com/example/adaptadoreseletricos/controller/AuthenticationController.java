package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.UsuarioRepository;
import com.example.adaptadoreseletricos.dto.usuario.LoginRespostaDTO;
import com.example.adaptadoreseletricos.dto.usuario.RegistroDTO;
import com.example.adaptadoreseletricos.dto.usuario.UsuarioDTO;
import com.example.adaptadoreseletricos.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody @Valid UsuarioDTO dto
    ){
        var usuarioSenha = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
        var auth = authenticationManager.authenticate(usuarioSenha);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginRespostaDTO(token));

    }

    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody @Valid RegistroDTO dto) {
        // Se usuário já cadastrado, informa erro ao usuário
        if (usuarioRepository.findByLogin(dto.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        // Registra informações na tabela de Pessoas e obtém id para salvar na tabela de usuários
        Pessoa pessoa = dto.pessoaCadastroDTO().toPessoa();
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // Criptografa senha e salva usuário no BD
        String senhaCriptografada = new BCryptPasswordEncoder().encode(dto.senha());
        Usuario usuario = new Usuario(dto.login(), senhaCriptografada, pessoaSalva);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }
}
