package com.example.adaptadoreseletricos.domain.repository.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoas;
import com.example.adaptadoreseletricos.domain.entity.endereco.EnderecosPessoasChave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecosPessoasRepository extends JpaRepository<
        EnderecosPessoas,
        EnderecosPessoasChave> {
}
