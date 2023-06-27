package com.example.adaptadoreseletricos.service.eletrodomestico;

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
    private EletrodomesticoRepository repository;

    @Transactional
    public EletrodomesticoDetalheDTO salvar(EletrodomesticoCadastroDTO dto) {
        Eletrodomestico eletroASalvar = dto.toEletrodomestico();
        Eletrodomestico eletroSalvo = this.repository.save(eletroASalvar);
        return new EletrodomesticoDetalheDTO(eletroSalvo);
    }
}
