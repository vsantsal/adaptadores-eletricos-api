package com.example.adaptadoreseletricos.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/eletrodomesticos")
public class EletrodomesticoController {

    @Autowired
    private EletrodomesticoService service;

    @PostMapping
    public ResponseEntity cadastrar(
            @RequestBody @Valid CadastroEletrodomesticoDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ){
        var dtoResposta = this.service.salvar(dto);
        var uri = uriComponentsBuilder.path("/eletrodomesticos/{id}").buildAndExpand(dtoResposta.id()).toUri();
        return ResponseEntity.created(uri).body(dtoResposta);
    }


}
