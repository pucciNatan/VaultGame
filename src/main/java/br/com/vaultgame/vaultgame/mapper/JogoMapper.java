package br.com.vaultgame.vaultgame.mapper;

import br.com.vaultgame.vaultgame.dto.jogos.JogoRequestDTO;
import br.com.vaultgame.vaultgame.dto.jogos.JogoResponseDTO;
import br.com.vaultgame.vaultgame.model.CategoriaModel;
import br.com.vaultgame.vaultgame.model.JogoModel;
import br.com.vaultgame.vaultgame.repository.CategoriaRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JogoMapper {
    private final CategoriaRepository categoriaRepository;

    public JogoMapper(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public JogoModel toEntity(JogoRequestDTO dto) {
        if (dto == null) return null;

        JogoModel jogo = new JogoModel();
        jogo.setTitulo(dto.titulo());
        jogo.setDescricao(dto.descricao());
        jogo.setDataLancamento(dto.dataLancamento());
        jogo.setPreco(dto.preco());

        if (dto.categoriasIds() != null) {
            List<CategoriaModel> categoriasEncontradas = categoriaRepository.findAllById(dto.categoriasIds());
            jogo.setCategorias(new HashSet<>(categoriasEncontradas));
        }

        return jogo;
    }

    public JogoResponseDTO toResponseDTO(JogoModel model) {
        if (model == null) return null;

        Set<Long> categoriasIds = model.getCategorias().stream()
                .map(CategoriaModel::getId)
                .collect(Collectors.toSet());

        return new JogoResponseDTO(
                model.getId(),
                model.getTitulo(),
                model.getDescricao(),
                model.getDataLancamento(),
                model.getPreco(),
                categoriasIds
        );
    }
}