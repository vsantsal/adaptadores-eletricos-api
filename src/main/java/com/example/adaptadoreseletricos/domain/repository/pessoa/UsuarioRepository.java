package com.example.adaptadoreseletricos.domain.repository.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByLogin(String login);
}
