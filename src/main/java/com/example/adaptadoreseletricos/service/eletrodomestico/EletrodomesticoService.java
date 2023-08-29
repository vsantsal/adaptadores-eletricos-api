package com.example.adaptadoreseletricos.service.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoas;
import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoasChave;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoasChave;
import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticosPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecosPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.dto.eletrodomestico.EletrodomesticoCadastroDTO;
import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticoRepository;
import com.example.adaptadoreseletricos.dto.eletrodomestico.EletrodomesticoDetalheDTO;
import com.example.adaptadoreseletricos.service.pessoa.RegistroUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EletrodomesticoService {

    private final String MENSAGEM_ERRO_NAO_ASSOCIACAO = "Eletrodoméstico e usuário não associados";
    @Autowired
    private EletrodomesticoRepository eletrodomesticoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EletrodomesticosPessoasRepository eletrodomesticosPessoasRepository;

    @Autowired
    private EnderecosPessoasRepository enderecosPessoasRepository;

    @Transactional
    public EletrodomesticoDetalheDTO salvar(EletrodomesticoCadastroDTO dto) {
        // salva eletrodoméstico
        Eletrodomestico eletroASalvar = dto.toEletrodomestico();
        Eletrodomestico eletroSalvo = this.eletrodomesticoRepository.save(eletroASalvar);

        // Salva associação de eletrodoméstico cadastrado ao usuário logado
        var pessoaLogada =  RegistroUsuarioService.getPessoaLogada();
        this.eletrodomesticosPessoasRepository.save(
                new EletrodomesticosPessoas(pessoaLogada, eletroSalvo)
        );

        return new EletrodomesticoDetalheDTO(eletroSalvo);

    }

    public EletrodomesticoDetalheDTO detalhar(Long id) {
        Eletrodomestico eletrodomestico = this.eletrodomesticoRepository.getReferenceById(id);
        return new EletrodomesticoDetalheDTO(eletrodomestico);
    }

    @Transactional
    public void excluir(Long id) {
        // Entidades envolvidas na tentativa de exclusão
        var pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        var eletroAExcluir = eletrodomesticoRepository.getReferenceById(id);
        var chaveAssociacao = new EletrodomesticosPessoasChave(
          pessoaLogada, eletroAExcluir
        );

        // Execução de procedimentos dependendo se endereço está associado ou não
        var associacao = eletrodomesticosPessoasRepository.getReferenceById(chaveAssociacao);
        associacao.desativar();
        eletrodomesticosPessoasRepository.save(associacao);

    }

    @Transactional
    public EletrodomesticoDetalheDTO atualizar(Long id, EletrodomesticoCadastroDTO dto) {
        // Entidades envolvidas na transação
        var pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        Eletrodomestico eletroAAtualizar = eletrodomesticoRepository.getReferenceById(id);
        var enderecoAAtualizar = enderecoRepository.getReferenceById(dto.idEndereco());

        // Se não há associação eletrodoméstico com pessoa, não permitir atualização
        if (!eletrodomesticosPessoasRepository.existsByIdAtivoTrue(
                new EletrodomesticosPessoasChave(pessoaLogada, eletroAAtualizar)
        )){
            throw new EntityNotFoundException(MENSAGEM_ERRO_NAO_ASSOCIACAO);
        }

        // Se não há associação endereço com pessoa, não permitir atualização
        if (!enderecosPessoasRepository.existsByIdAtivoTrue(
                new EnderecosPessoasChave(pessoaLogada, enderecoAAtualizar)
        )){
            throw new EntityNotFoundException("Endereço não associado ao usuário");
        }

        // Atualiza dados
        eletroAAtualizar.setNome(dto.nome());
        eletroAAtualizar.setMarca(dto.marca());
        eletroAAtualizar.setModelo(dto.modelo());
        eletroAAtualizar.setPotencia(dto.potencia());
        eletroAAtualizar.setEndereco(enderecoAAtualizar);

        eletrodomesticoRepository.save(eletroAAtualizar);

        return new EletrodomesticoDetalheDTO(eletroAAtualizar);

    }

    public List<EletrodomesticoDetalheDTO> listar(EletrodomesticoCadastroDTO paramPesquisa) {
        // entidades envolvidas na requisição
        var pessoaLogada = RegistroUsuarioService.getPessoaLogada();
        Eletrodomestico eletrodomesticoPesquisado = paramPesquisa.toEletrodomestico();
        Example<Eletrodomestico> exemplo = Example.of(eletrodomesticoPesquisado);
        List<Eletrodomestico> eletros = eletrodomesticoRepository.findAll(exemplo);
        return eletros
                .stream()
                .filter(eletrodomestico -> eletrodomesticosPessoasRepository.existsByIdAtivoTrue(
                        new EletrodomesticosPessoasChave(pessoaLogada, eletrodomestico)
                ))
                .map(EletrodomesticoDetalheDTO::new)
                .toList();
    }
}
