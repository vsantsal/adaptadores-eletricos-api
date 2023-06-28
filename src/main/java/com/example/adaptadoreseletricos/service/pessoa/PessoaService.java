package com.example.adaptadoreseletricos.service.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaCadastroDTO;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaDetalheDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    @Transactional
    public PessoaDetalheDTO salvar(PessoaCadastroDTO dto) {
        Pessoa pessoaASalvar = dto.toPessoa();
        Pessoa pessoaSalva = this.repository.save(pessoaASalvar);
        return new PessoaDetalheDTO(pessoaSalva);
    }
}
