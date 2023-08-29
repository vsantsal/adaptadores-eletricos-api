package com.example.adaptadoreseletricos.service.eletrodomestico;

import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticosPessoasRepository;
import com.example.adaptadoreseletricos.domain.repository.endereco.EnderecoRepository;
import com.example.adaptadoreseletricos.dto.eletrodomestico.EletrodomesticoCadastroDTO;
import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import com.example.adaptadoreseletricos.domain.repository.eletrodomestico.EletrodomesticoRepository;
import com.example.adaptadoreseletricos.dto.eletrodomestico.EletrodomesticoDetalheDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EletrodomesticoService {
    @Autowired
    private EletrodomesticoRepository eletrodomesticoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EletrodomesticosPessoasRepository eletrodomesticosPessoasRepository;


    @Transactional
    public EletrodomesticoDetalheDTO salvar(EletrodomesticoCadastroDTO dto) {
        Eletrodomestico eletroASalvar = dto.toEletrodomestico();
        Eletrodomestico eletroSalvo = this.eletrodomesticoRepository.save(eletroASalvar);
        return new EletrodomesticoDetalheDTO(eletroSalvo);
    }

    public EletrodomesticoDetalheDTO detalhar(Long id) {
        Eletrodomestico eletrodomestico = this.eletrodomesticoRepository.getReferenceById(id);
        return new EletrodomesticoDetalheDTO(eletrodomestico);
    }
}
