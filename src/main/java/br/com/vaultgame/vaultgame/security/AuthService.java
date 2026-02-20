package br.com.vaultgame.vaultgame.security;

import br.com.vaultgame.vaultgame.dto.Usuario.LoginRequestDTO;
import br.com.vaultgame.vaultgame.dto.Usuario.LoginResponseDTO;
import br.com.vaultgame.vaultgame.dto.Usuario.UsuarioResponseDTO;
import br.com.vaultgame.vaultgame.mapper.UsuarioMapper;
import br.com.vaultgame.vaultgame.model.UsuarioModel;
import br.com.vaultgame.vaultgame.repository.UsuarioRepository;
import br.com.vaultgame.vaultgame.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UsuarioMapper usuarioMapper;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.usuarioMapper = usuarioMapper;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        UsuarioModel usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }

        String token = jwtUtils.generateToken(usuario.getEmail());
        UsuarioResponseDTO usuarioDTO = usuarioMapper.toResponseDTO(usuario);

        return new LoginResponseDTO(token, usuarioDTO);
    }
}
