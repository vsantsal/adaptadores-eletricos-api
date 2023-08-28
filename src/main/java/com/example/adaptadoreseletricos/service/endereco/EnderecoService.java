package com.example.adaptadoreseletricos.service.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoas;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecosPessoasRepository;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoCadastroDTO;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoDetalheDTO;
import com.example.adaptadoreseletricos.service.pessoa.RegistroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecosPessoasRepository enderecosPessoasRepository;

    @Transactional
    public EnderecoDetalheDTO salvar(EnderecoCadastroDTO dto) {

        // Salva endereço informado pelo usuário
        Endereco enderecoASalvar = dto.toEndereco();
        Endereco enderecoSalvo = this.enderecoRepository.save(enderecoASalvar);

        // Salva associação de endereço cadastrado ao usuário logado
        Pessoa pessoa = RegistroUsuarioService.getPessoaLogada();
        this.enderecosPessoasRepository.save(
                new EnderecosPessoas(pessoa, enderecoSalvo)
        );

        return new EnderecoDetalheDTO(enderecoSalvo);
    }

    public EnderecoDetalheDTO detalhar(Long id) {
        Endereco endereco = enderecoRepository.getReferenceById(id);
        return new EnderecoDetalheDTO(endereco);
    }
}
