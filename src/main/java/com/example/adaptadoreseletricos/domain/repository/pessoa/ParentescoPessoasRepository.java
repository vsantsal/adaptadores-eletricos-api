package com.example.adaptadoreseletricos.domain.repository.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.ParentescoPessoas;
import com.example.adaptadoreseletricos.domain.entity.pessoa.ParentescoPessoasChave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentescoPessoasRepository extends JpaRepository<
        ParentescoPessoas,
        ParentescoPessoasChave> {
}
