package br.com.vaultgame.vaultgame.service;

import br.com.vaultgame.vaultgame.dto.categorias.CategoriaRequestDTO;
import br.com.vaultgame.vaultgame.dto.categorias.CategoriaResponseDTO;
import br.com.vaultgame.vaultgame.exception.ResourceNotFoundException;
import br.com.vaultgame.vaultgame.mapper.CategoriaMapper;
import br.com.vaultgame.vaultgame.model.CategoriaModel;
import br.com.vaultgame.vaultgame.model.JogoModel;
import br.com.vaultgame.vaultgame.repository.CategoriaRepository;
import br.com.vaultgame.vaultgame.repository.JogoRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class CategoriaService {
    final CategoriaMapper categoriaMapper;
    final CategoriaRepository categoriaRepository;
    final JogoRepository jogoRepository;

    public CategoriaService(CategoriaMapper categoriaMapper, CategoriaRepository categoriaRepository, JogoRepository jogoRepository) {
        this.categoriaMapper = categoriaMapper;
        this.categoriaRepository = categoriaRepository;
        this.jogoRepository = jogoRepository;
    }

    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO categoriaRequestDTO) {
        CategoriaModel novaCategoria = categoriaMapper.toEntity(categoriaRequestDTO);
        CategoriaModel categoriaSalva = categoriaRepository.save(novaCategoria);
        if (categoriaRequestDTO.jogosIds() != null && !categoriaRequestDTO.jogosIds().isEmpty()) {
            var jogos = jogoRepository.findAllById(categoriaRequestDTO.jogosIds());
            for (JogoModel jogo : jogos) {
                jogo.getCategorias().add(categoriaSalva);
                jogoRepository.save(jogo);
            }
            categoriaSalva.setJogos(new HashSet<>(jogos));
        }
        return categoriaMapper.toResponseDTO(categoriaSalva);
    }

    public CategoriaResponseDTO getCategoriaById(Long id) {
        CategoriaModel categoriaEncontrada = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return categoriaMapper.toResponseDTO(categoriaEncontrada);
    }

    public List<CategoriaResponseDTO> getAllCategorias() {
        List<CategoriaModel> listTodasCategorias = categoriaRepository.findAll();

        return listTodasCategorias.stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

    public void deleteCategoriaById(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", id);
        }
        categoriaRepository.deleteById(id);
    }

    public CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO categoriaAtualizada) {
        CategoriaModel categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        categoriaExistente.setNome(categoriaAtualizada.nome());
        categoriaExistente.setDescricao(categoriaAtualizada.descricao());

        if (categoriaAtualizada.jogosIds() != null) {
            var jogosEncontrados = jogoRepository.findAllById(categoriaAtualizada.jogosIds());
            var jogosParaSalvar = new HashSet<JogoModel>();

            for (JogoModel jogo : categoriaExistente.getJogos()) {
                if (!jogosEncontrados.contains(jogo)) {
                    jogo.getCategorias().remove(categoriaExistente);
                    jogosParaSalvar.add(jogo);
                }
            }
            for (JogoModel jogo : jogosEncontrados) {
                jogo.getCategorias().add(categoriaExistente);
                jogosParaSalvar.add(jogo);
            }
            jogoRepository.saveAll(jogosParaSalvar);
            categoriaExistente.setJogos(new HashSet<>(jogosEncontrados));
        }

        CategoriaModel salva = categoriaRepository.save(categoriaExistente);
        return categoriaMapper.toResponseDTO(salva);
    }
}
