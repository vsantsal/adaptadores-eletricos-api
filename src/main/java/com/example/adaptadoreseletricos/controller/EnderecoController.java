package com.example.adaptadoreseletricos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    @PostMapping
    public ResponseEntity cadastrarEndereco(
            @RequestBody @Valid CadastroEnderecoDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ){
        var dtoResposta = this.service.salvar(dto);
        var uri = uriBuilder.path("/enderecos/{id}").buildAndExpand(dtoResposta.id()).toUri();
        return RResponseEntity.created(uri).body(dtoResposta);
    }
}
