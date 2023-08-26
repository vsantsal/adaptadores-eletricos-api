package com.example.adaptadoreseletricos.domain.repository.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Parentesco;
import com.example.adaptadoreseletricos.domain.entity.pessoa.ParentescoPessoas;
import com.example.adaptadoreseletricos.domain.entity.pessoa.ParentescoPessoasChave;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParentescoPessoasRepository extends JpaRepository<
        ParentescoPessoas,
        ParentescoPessoasChave> {

    @Modifying
    @Query(value = "delete from ParentescoPessoas p where p.pessoa1.id = ?1 or p.pessoa2.id= ?1")
    void removerTodosParentescosDePessoa(Long id);

    @Modifying
    @Query(value = "update ParentescoPessoas p set p.parentesco = ?3 where p.pessoa1.id = ?1 and p.pessoa2.id = ?2")
    void  atualizarParentescoEntrePessoas(Long idPessoa1, Long idPessoa2, Parentesco parentesco);
}
