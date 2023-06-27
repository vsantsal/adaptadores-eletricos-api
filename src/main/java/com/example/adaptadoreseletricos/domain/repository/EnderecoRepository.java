package com.example.adaptadoreseletricos.domain.repository;

import com.example.adaptadoreseletricos.domain.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
