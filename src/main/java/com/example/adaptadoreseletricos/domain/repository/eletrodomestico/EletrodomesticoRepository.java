package com.example.adaptadoreseletricos.domain.repository.eletrodomestico;

import com.example.adaptadoreseletricos.domain.entity.eletrodomestico.Eletrodomestico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EletrodomesticoRepository extends JpaRepository<Eletrodomestico, Long> {
}
