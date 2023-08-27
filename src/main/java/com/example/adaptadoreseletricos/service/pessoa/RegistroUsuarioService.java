package com.example.adaptadoreseletricos.service.pessoa;

import com.example.adaptadoreseletricos.domain.entity.pessoa.Pessoa;
import com.example.adaptadoreseletricos.domain.entity.pessoa.Usuario;
import com.example.adaptadoreseletricos.domain.repository.pessoa.PessoaRepository;
import com.example.adaptadoreseletricos.domain.repository.pessoa.UsuarioRepository;
import com.example.adaptadoreseletricos.dto.pessoa.PessoaDetalheDTO;
import com.example.adaptadoreseletricos.dto.usuario.RegistroDTO;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public PessoaDetalheDTO registrar(RegistroDTO dto){

        // Se usuário já cadastrado, informa erro ao usuário
        if (usuarioRepository.findByLogin(dto.login()) != null) {
            throw new EntityExistsException("Usuário já cadastrado");
        }

        // Registra informações na tabela de Pessoas e obtém id para salvar na tabela de usuários
        Pessoa pessoa = dto.pessoaCadastroDTO().toPessoa();
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // Criptografa senha e salva usuário no BD
        String senhaCriptografada = new BCryptPasswordEncoder().encode(dto.senha());
        Usuario usuario = new Usuario(dto.login(), senhaCriptografada, pessoaSalva);
        usuarioRepository.save(usuario);

        return new PessoaDetalheDTO(pessoaSalva);
    }

}
