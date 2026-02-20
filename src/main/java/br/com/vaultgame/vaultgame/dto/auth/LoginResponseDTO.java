package br.com.vaultgame.vaultgame.dto.Usuario;

public record LoginResponseDTO(
        String token,
        UsuarioResponseDTO usuario
) {}
