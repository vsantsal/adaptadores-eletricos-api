package com.example.adaptadoreseletricos.domain.repository.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoas;
import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.EletrodomesticosPessoasChave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EletrodomesticosPessoasRepository extends JpaRepository<
        EletrodomesticosPessoas, EletrodomesticosPessoasChave> {

    @Query("select count(eletrosPessoas) > 0 from EletrodomesticosPessoas eletrosPessoas where eletrosPessoas.id = ?1 and eletrosPessoas.ativo = true")
    boolean existsByIdAtivoTrue(EletrodomesticosPessoasChave id);

}
