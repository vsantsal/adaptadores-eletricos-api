package com.example.adaptadoreseletricos.service.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoas;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoasChave;
import com.example.adaptadoreseletricos.domain.entity.endereco.Estado;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecosPessoasRepository;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoCadastroDTO;
import com.example.adaptadoreseletricos.dto.endereco.EnderecoDetalheDTO;
import com.example.adaptadoreseletricos.service.pessoa.RegistroUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoService {

    private final String MENSAGEM_ERRO_NAO_ASSOCIACAO = "Endereço e usuário não associados";
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
        // entidades envolvidas na transação
        var pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        Endereco endereco = enderecoRepository.getReferenceById(id);

        if (enderecosPessoasRepository.existsById(
                new EnderecosPessoasChave(
                        pessoaLogada,
                        endereco
                ))){
            return new EnderecoDetalheDTO(endereco);
        }

        throw new EntityNotFoundException(MENSAGEM_ERRO_NAO_ASSOCIACAO);

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
            var associacao = enderecosPessoasRepository.getReferenceById(chaveAssociacao);
            associacao.desativar();
            enderecosPessoasRepository.save(associacao);
        } else {
            throw new EntityNotFoundException(MENSAGEM_ERRO_NAO_ASSOCIACAO);
        }
    }

    @Transactional
    public EnderecoDetalheDTO atualizar(Long id, EnderecoCadastroDTO dto) {
        // Entidades envolvidas na transação
        Endereco enderecoAAtualizar = enderecoRepository.getReferenceById(id);
        var pessoaLogada = RegistroUsuarioService.getPessoaLogada();

        // Se não há associação, não permitir atualização
        if (!enderecosPessoasRepository.existsById(
                new EnderecosPessoasChave(pessoaLogada, enderecoAAtualizar)
        )){
            throw new EntityNotFoundException(MENSAGEM_ERRO_NAO_ASSOCIACAO);
        }

        // Atualiza dados do endereço
        enderecoAAtualizar.setBairro(dto.bairro());
        enderecoAAtualizar.setRua(dto.rua());
        enderecoAAtualizar.setCidade(dto.cidade());
        enderecoAAtualizar.setNumero(dto.numero());
        enderecoAAtualizar.setEstado(Estado.valueOf(dto.estado()));

        enderecoRepository.save(enderecoAAtualizar);
        return new EnderecoDetalheDTO(enderecoAAtualizar);

    }

    public List<EnderecoDetalheDTO> listar(EnderecoCadastroDTO paramPesquisa) {
        // entidades envolvidas na requisição
        var pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        Endereco enderecoPesquisado = paramPesquisa.toEndereco();
        Example<Endereco> exemplo = Example.of(enderecoPesquisado);
        List<Endereco> enderecos = enderecoRepository.findAll(exemplo);
        return enderecos
                .stream()
                .filter(e -> enderecosPessoasRepository.existsById(
                            new EnderecosPessoasChave(pessoaLogada, e)
                        )
                )
                .map(EnderecoDetalheDTO::new)
                .toList();

    }
}
