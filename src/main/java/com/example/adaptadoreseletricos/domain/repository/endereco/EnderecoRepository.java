package com.example.adaptadoreseletricos.domain.repository.endereco;

import com.example.adaptadoreseletricos.domain.entity.endereco.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
