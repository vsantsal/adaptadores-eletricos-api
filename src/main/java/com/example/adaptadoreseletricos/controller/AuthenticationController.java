package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import com.example.adaptadoreseletricos.domain.repository.pessoa.UsuarioRepository;
import com.example.adaptadoreseletricos.dto.usuario.LoginRespostaDTO;
import com.example.adaptadoreseletricos.dto.usuario.RegistroDTO;
import com.example.adaptadoreseletricos.dto.usuario.UsuarioDTO;
import com.example.adaptadoreseletricos.infra.security.TokenService;
import com.example.adaptadoreseletricos.service.pessoa.RegistroUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    RegistroUsuarioService registroService;

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
        registroService.registrar(dto);
        return ResponseEntity.ok().build();
    }
}
