package com.example.adaptadoreseletricos.domain.repository.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoas;
import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoasChave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EletrodomesticosPessoasRepository extends JpaRepository<
        EletrodomesticosPessoas, EletrodomesticosPessoasChave> {
}
