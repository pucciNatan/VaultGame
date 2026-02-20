package br.com.vaultgame.vaultgame.mapper;

import br.com.vaultgame.vaultgame.dto.Usuario.UsuarioRequestDTO;
import br.com.vaultgame.vaultgame.dto.Usuario.UsuarioResponseDTO;
import br.com.vaultgame.vaultgame.model.UsuarioModel;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toResponseDTO(UsuarioModel usuarioModel){
        UsuarioResponseDTO dto = new UsuarioResponseDTO(
                usuarioModel.getId(),
                usuarioModel.getEmail(),
                usuarioModel.getPrimeiroNome(),
                usuarioModel.getUltimoNome()
        );

        return dto;
    }

    public UsuarioModel toEntity(UsuarioRequestDTO dto){
        UsuarioModel usuarioModel = new UsuarioModel();

        usuarioModel.setEmail(dto.email());
        usuarioModel.setPrimeiroNome(dto.primeiroNome());
        usuarioModel.setUltimoNome(dto.ultimoNome());

        return usuarioModel;
    }
}
