package com.example.adaptadoreseletricos.service.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.*;
import com.example.adaptadoreseletricos.domain.repository.pessoa.ParentescoPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaCadastroDTO;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaDetalheDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaService {

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

        // Salva relacionamentos de parentesco, caso tenha sido informado
        if (dto.parentesco() != null) {
            Parentesco parentesco = Parentesco.valueOf(dto.parentesco());
            var usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var pessoaLogada = usuario.getPessoa();
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
        Pessoa pessoa = pessoaRepository.getReferenceById(id);
        return new PessoaDetalheDTO(pessoa);
    }

    @Transactional
    public void excluir(Long id) {
        var pessoaAExcluir = this.pessoaRepository.getReferenceById(id);
        this.parentescoPessoasRepository.deleteByPessoa1(pessoaAExcluir);
        this.parentescoPessoasRepository.deleteByPessoa2(pessoaAExcluir);
        this.pessoaRepository.deleteById(id);
    }
}
