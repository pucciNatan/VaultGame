package br.com.vaultgame.vaultgame.mapper;

import br.com.vaultgame.vaultgame.dto.categorias.CategoriaRequestDTO;
import br.com.vaultgame.vaultgame.dto.categorias.CategoriaResponseDTO;
import br.com.vaultgame.vaultgame.model.CategoriaModel;
import br.com.vaultgame.vaultgame.model.JogoModel;
import br.com.vaultgame.vaultgame.repository.JogoRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoriaMapper {
    private final JogoRepository jogoRepository;

    public CategoriaMapper(JogoRepository jogoRepository){
        this.jogoRepository = jogoRepository;
    }

    public CategoriaModel toEntity(CategoriaRequestDTO dto){
        if (dto==null) return null;

        CategoriaModel categoriaModel = new CategoriaModel();
        categoriaModel.setNome(dto.nome());
        categoriaModel.setDescricao(dto.descricao());

        if(dto.jogosIds() != null) {
            List<JogoModel> jogosEncontrados = jogoRepository.findAllById(dto.jogosIds());
            categoriaModel.setJogos(new HashSet<>(jogosEncontrados));
        }

        return categoriaModel;
    }

    public CategoriaResponseDTO toResponseDTO(CategoriaModel model) {
        if (model == null) return null;

        Set<Long> jogosIds = model.getJogos().stream()
                .map(JogoModel::getId)
                .collect(Collectors.toSet());

        return new CategoriaResponseDTO(
                model.getId(),
                model.getNome(),
                model.getDescricao(),
                jogosIds
        );
    }
}
