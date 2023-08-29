package com.example.adaptadoreseletricos.domain.repository.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoas;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoasChave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EnderecosPessoasRepository extends JpaRepository<
        EnderecosPessoas,
        EnderecosPessoasChave> {

    @Query("select count(residencias) > 0 from EnderecosPessoas residencias where residencias.id = ?1 and residencias.ativo = true")
    boolean existsByIdAtivoTrue(EnderecosPessoasChave id);
}
