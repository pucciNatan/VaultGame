package br.com.vaultgame.vaultgame.security;

import br.com.vaultgame.vaultgame.dto.Usuario.LoginRequestDTO;
import br.com.vaultgame.vaultgame.dto.Usuario.LoginResponseDTO;
import br.com.vaultgame.vaultgame.dto.Usuario.UsuarioRequestDTO;
import br.com.vaultgame.vaultgame.dto.Usuario.UsuarioResponseDTO;
import br.com.vaultgame.vaultgame.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO register(@RequestBody @Valid UsuarioRequestDTO request) {
        return usuarioService.registrarUsuario(request);
    }
}
