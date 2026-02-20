package br.com.vaultgame.vaultgame.dto.Usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(

        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Primeiro nome é obrigatório")
        String primeiroNome,

        @NotBlank(message = "último nome é obrigatório")
        String ultimoNome,

        @NotBlank(message = "Senha é obrigatória")
        String password
){}
