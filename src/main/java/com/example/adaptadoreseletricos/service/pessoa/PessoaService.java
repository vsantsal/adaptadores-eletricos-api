package com.example.adaptadoreseletricos.service.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.*;
import com.example.adaptadoreseletricos.domain.repository.pessoa.ParentescoPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaCadastroDTO;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaComParentescoDTO;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaDetalheDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PessoaService {

    private final String MENSAGEM_ERRO_NAO_ASSOCIACAO = "Pessoas não associadas";

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ParentescoPessoasRepository parentescoPessoasRepository;

    @Transactional
    public PessoaDetalheDTO salvar(PessoaCadastroDTO dto) {
        // Valida sexo e parentesco no DTO
        if (dto.parentesco() != null &&
                !(Parentesco.valueOf(
                        dto.parentesco()).getSexoCorrespondente()
                        .equals(Sexo.valueOf(dto.sexo())))) {
            throw new IllegalArgumentException("Sexo e Parentesco informados incompatíveis");
        }

        // Salva pessoa informada pelo usuário logado
        Pessoa pessoaASalvar = dto.toPessoa();
        Pessoa pessoaSalva = this.pessoaRepository.save(pessoaASalvar);
        Pessoa pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        // Salva relacionamentos de parentesco, caso tenha sido informado
        if (dto.parentesco() != null) {
            Parentesco parentesco = Parentesco.valueOf(dto.parentesco());
            // salva relacionamento da pessoa cadastrada com o usuário logado
            this.parentescoPessoasRepository.save(
                    new ParentescoPessoas(
                            pessoaLogada,
                            pessoaSalva,
                            parentesco)
            );
            // Também salva o relacionamento do usuário logado com a pessoa cadastrada
            this.parentescoPessoasRepository.save(
                    new ParentescoPessoas(
                            pessoaSalva,
                            pessoaLogada,
                            parentesco.getInversaoDeParentesco(pessoaLogada.getSexo()))
            );

        }
        return new PessoaDetalheDTO(pessoaSalva);
    }

    public PessoaDetalheDTO detalhar(Long id) {
        // Entidades envolvidas na requisição
        Pessoa pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        Pessoa pessoa = pessoaRepository.getReferenceById(id);
        if (parentescoPessoasRepository.existsById(
                new ParentescoPessoasChave(pessoaLogada, pessoa)
        )){
            return new PessoaDetalheDTO(pessoa);
        }
        throw new EntityNotFoundException(MENSAGEM_ERRO_NAO_ASSOCIACAO);
    }

    @Transactional
    public void excluir(Long id) {
        this.parentescoPessoasRepository
                .removerTodosParentescosDePessoa(id);
        this.pessoaRepository.deleteById(id);
    }

    @Transactional
    public PessoaComParentescoDTO atualizar(Long id, PessoaCadastroDTO dto) {
        // Atualiza dados da pessoa
        Pessoa pessoaAAtualizar = pessoaRepository.getReferenceById(id);
        pessoaAAtualizar.setNome(dto.nome());
        pessoaAAtualizar.setSexo(Sexo.valueOf(dto.sexo()));
        pessoaAAtualizar.setDataNascimento(dto.dataNascimento());

        // Atualiza parentesco com usuário
        Pessoa pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        Parentesco novoParentesco = Parentesco.valueOf(dto.parentesco());
        Parentesco novoInversoDeParentesco = novoParentesco.getInversaoDeParentesco(
                pessoaLogada.getSexo()
        );
        this.parentescoPessoasRepository.atualizarParentescoEntrePessoas(
                pessoaLogada.getId(),
                pessoaAAtualizar.getId(),
                novoParentesco
        );

        this.parentescoPessoasRepository.atualizarParentescoEntrePessoas(
                pessoaAAtualizar.getId(),
                pessoaLogada.getId(),
                novoInversoDeParentesco
        );

        pessoaRepository.save(pessoaAAtualizar);

        return new PessoaComParentescoDTO(pessoaAAtualizar, novoParentesco);

    }

    public List<PessoaComParentescoDTO> listar(PessoaCadastroDTO paramPesquisa) {
        Pessoa pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        Pessoa pessoa = paramPesquisa.toPessoa();
        Parentesco parentescoPesquisa = paramPesquisa.parentesco() != null ? Parentesco.valueOf(paramPesquisa.parentesco()): null;
        Example<Pessoa> exemplo = Example.of(pessoa);
        List<Pessoa> parentes = pessoaRepository.findAll(exemplo);
        return parentes
                .stream()
                .filter(
                        p -> (parentescoPesquisa == null ||
                                parentescoPessoasRepository.obterParentescoParaPessoas(
                                        pessoaLogada.getId(),
                                        p.getId()) == parentescoPesquisa
                        )
                )
                .filter(p ->
                        parentescoPessoasRepository.obterParentescoParaPessoas(
                                pessoaLogada.getId(), p.getId()
                        ) != null)
                .map(
                        p -> new PessoaComParentescoDTO(
                                p,
                                parentescoPessoasRepository.obterParentescoParaPessoas(
                                        pessoaLogada.getId(), p.getId()
                                )
                        )

                ).toList();
    }


}
