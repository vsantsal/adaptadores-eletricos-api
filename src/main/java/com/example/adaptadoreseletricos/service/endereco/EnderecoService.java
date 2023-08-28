package com.example.adaptadoreseletricos.service.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoas;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoasChave;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecosPessoasRepository;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoCadastroDTO;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoDetalheDTO;
import com.example.adaptadoreseletricos.service.pessoa.RegistroUsuarioService;
import jakarta.persistence.EntityNotFoundException;
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
        var pessoaLogada =  RegistroUsuarioService.getPessoaLogada();
        this.enderecosPessoasRepository.save(
                new EnderecosPessoas(pessoaLogada, enderecoSalvo)
        );

        return new EnderecoDetalheDTO(enderecoSalvo);
    }

    public EnderecoDetalheDTO detalhar(Long id) {
        Endereco endereco = enderecoRepository.getReferenceById(id);
        return new EnderecoDetalheDTO(endereco);
    }

    @Transactional
    public void excluir(Long id) {
        // Entidades envolvidas na tentativa de exclusão
        var pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        var enderecoAExcluir = enderecoRepository.getReferenceById(id);
        var chaveAssociacao = new EnderecosPessoasChave(
                pessoaLogada,
                enderecoAExcluir
        );
        boolean enderecoDaPessoaLogada = enderecosPessoasRepository.existsById(chaveAssociacao);

        // Execução de procedimentos dependendo se endereço está associado ou não
        // Ao usuário logado na aplicação
        if (enderecoDaPessoaLogada) {
            this.enderecosPessoasRepository.deleteById(chaveAssociacao);
            this.enderecoRepository.delete(enderecoAExcluir);
        } else {
            throw new EntityNotFoundException("Endereço e usuário não associados");
        }
    }
}
