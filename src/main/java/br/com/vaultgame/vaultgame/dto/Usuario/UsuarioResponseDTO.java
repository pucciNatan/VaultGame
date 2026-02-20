package br.com.vaultgame.vaultgame.dto.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String email,
        String primeiroNome,
        String ultimoNome
){}
