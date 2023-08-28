package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.dto.pessoa.PessoaCadastroDTO;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaComParentescoDTO;
import com.example.adaptadoreseletricos.service.pessoa.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
    @Autowired
    private PessoaService service;

    @PostMapping
    public ResponseEntity cadastrar(
            @RequestBody @Valid PessoaCadastroDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ){
        var dtoResposta = this.service.salvar(dto);
        var uri = uriComponentsBuilder.path("/pessoas/{id}").buildAndExpand(dtoResposta.id()).toUri();
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

    @PutMapping("/{id}")
    public ResponseEntity atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PessoaCadastroDTO dto
            ){
        var dtoResposta = this.service.atualizar(id, dto);
        return ResponseEntity.ok(dtoResposta);
    }

    @GetMapping
    public ResponseEntity listar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "dataNascimento", required = false) LocalDate dataNascimento,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "parentesco", required = false) String parentesco
    ){
        PessoaCadastroDTO paramPesquisa = new PessoaCadastroDTO(nome, dataNascimento, sexo, parentesco);
        List<PessoaComParentescoDTO> parentesEncontrados = service.listar(paramPesquisa);
        return ResponseEntity.ok(parentesEncontrados);
    }


}
