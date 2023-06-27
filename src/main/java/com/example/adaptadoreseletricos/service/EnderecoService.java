package com.example.adaptadoreseletricos.service;

import com.example.adaptadoreseletricos.domain.entity.Endereco;
import com.example.adaptadoreseletricos.domain.repository.EnderecoRepository;
import com.example.adaptadoreseletricos.dto.CadastroEnderecoDTO;
import com.example.adaptadoreseletricos.dto.EnderecoDetalheDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    @Transactional
    public EnderecoDetalheDTO salvar(CadastroEnderecoDTO dto) {
        Endereco enderecoASalvar = dto.toEndereco();
        Endereco enderecoSalvo = this.repository.save(enderecoASalvar);
        return new EnderecoDetalheDTO(enderecoSalvo);
    }
}
