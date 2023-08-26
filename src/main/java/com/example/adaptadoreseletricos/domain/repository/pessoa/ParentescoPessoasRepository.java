package com.example.adaptadoreseletricos.domain.repository.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.ParentescoPessoas;
import com.example.adaptadoreseletricos.domain.entity.pessoa.ParentescoPessoasChave;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParentescoPessoasRepository extends JpaRepository<
        ParentescoPessoas,
        ParentescoPessoasChave> {

    void deleteByPessoa1(Pessoa pessoa);

    void deleteByPessoa2(Pessoa pessoa);
}
