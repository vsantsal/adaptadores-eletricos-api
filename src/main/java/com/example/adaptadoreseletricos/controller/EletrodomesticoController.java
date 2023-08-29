package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.dto.eletrodomestico.EletrodomesticoCadastroDTO;
import com.example.adaptadoreseletricos.service.eletrodomestico.EletrodomesticoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/eletrodomesticos")
public class EletrodomesticoController {

    @Autowired
    private EletrodomesticoService service;

    @PostMapping
    public ResponseEntity cadastrar(
            @RequestBody @Valid EletrodomesticoCadastroDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ){
        var dtoResposta = this.service.salvar(dto);
        var uri = uriComponentsBuilder.path("/eletrodomesticos/{id}").buildAndExpand(dtoResposta.id()).toUri();
        return ResponseEntity.created(uri).body(dtoResposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(
            @PathVariable Long id
    ){
        var dto = this.service.detalhar(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id){
        this.service.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
