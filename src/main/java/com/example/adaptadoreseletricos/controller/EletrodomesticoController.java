package com.example.adaptadoreseletricos.controller;

import com.example.adaptadoreseletricos.dto.eletrodomestico.EletrodomesticoCadastroDTO;
import com.example.adaptadoreseletricos.dto.eletrodomestico.EletrodomesticoDetalheDTO;
import com.example.adaptadoreseletricos.service.eletrodomestico.EletrodomesticoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    @PutMapping("/{id}")
    public ResponseEntity atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EletrodomesticoCadastroDTO dto
    ){
        var dtoResposta = this.service.atualizar(id, dto);
        return ResponseEntity.ok(dtoResposta);
    }

    @GetMapping
    public ResponseEntity listar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "potencia", required = false) Long potencia,
            @RequestParam(value = "idEndereco", required = false) Long idEndereco
    ){
        EletrodomesticoCadastroDTO paramPesquisa = new EletrodomesticoCadastroDTO(nome, modelo, marca, potencia, idEndereco);
        List<EletrodomesticoDetalheDTO> eletros = service.listar(paramPesquisa);
        return ResponseEntity.ok(eletros);
    }


}
