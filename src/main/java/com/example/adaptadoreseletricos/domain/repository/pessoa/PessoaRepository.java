package com.example.adaptadoreseletricos.domain.repository.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
