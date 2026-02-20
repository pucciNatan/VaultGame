package br.com.vaultgame.vaultgame.service;

import br.com.vaultgame.vaultgame.dto.Usuario.UsuarioRequestDTO;
import br.com.vaultgame.vaultgame.dto.Usuario.UsuarioResponseDTO;
import br.com.vaultgame.vaultgame.exception.EmailAlreadyExistsException;
import br.com.vaultgame.vaultgame.mapper.UsuarioMapper;
import br.com.vaultgame.vaultgame.model.UsuarioModel;
import br.com.vaultgame.vaultgame.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    final UsuarioRepository usuarioRepository;
    final PasswordEncoder passwordEncoder;
    final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }
        String senhaCriptografada = passwordEncoder.encode(dto.password());
        UsuarioModel novoUsuario = usuarioMapper.toEntity(dto);
        novoUsuario.setPassword(senhaCriptografada);

        UsuarioModel salvo = usuarioRepository.save(novoUsuario);
        return usuarioMapper.toResponseDTO(salvo);
    }
}
