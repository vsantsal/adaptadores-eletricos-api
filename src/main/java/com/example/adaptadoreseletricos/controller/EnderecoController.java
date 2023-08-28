package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.dto.endereco.EnderecoCadastroDTO;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoDetalheDTO;
import com.example.adaptadoreseletricos.service.endereco.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @PostMapping
    public ResponseEntity cadastrar(
            @RequestBody @Valid EnderecoCadastroDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ){
        var dtoResposta = this.service.salvar(dto);
        var uri = uriComponentsBuilder.path("/enderecos/{id}").buildAndExpand(dtoResposta.id()).toUri();
        return ResponseEntity.created(uri).body(dtoResposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(
            @PathVariable Long id
    ){
        var dto = this.service.detalhar(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EnderecoCadastroDTO dto
    ){
        var dtoResposta = this.service.atualizar(id, dto);
        return ResponseEntity.ok(dtoResposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id){
        this.service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity listar(
            @RequestParam(value = "rua", required = false) String rua,
            @RequestParam(value = "numero", required = false) Long numero,
            @RequestParam(value = "bairro", required = false) String bairro,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "estado", required = false) String estado
    ){
        EnderecoCadastroDTO paramPesquisa = new EnderecoCadastroDTO(rua, numero, bairro, cidade, estado);
        List<EnderecoDetalheDTO> enderecos = service.listar(paramPesquisa);
        return ResponseEntity.ok(enderecos);
    }

}
