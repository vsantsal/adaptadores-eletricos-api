package com.example.adaptadoreseletricos.service.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.dto.endereco.CadastroEnderecoDTO;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoDetalheDTO;
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

    public EnderecoDetalheDTO detalhar(Long id) {
        Endereco endereco = repository.getReferenceById(id);
        return new EnderecoDetalheDTO(endereco);
    }
}
