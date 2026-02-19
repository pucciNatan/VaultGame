package br.com.vaultgame.vaultgame.service;

import br.com.vaultgame.vaultgame.dto.jogos.JogoRequestDTO;
import br.com.vaultgame.vaultgame.dto.jogos.JogoResponseDTO;
import br.com.vaultgame.vaultgame.exception.ResourceNotFoundException;
import br.com.vaultgame.vaultgame.mapper.JogoMapper;
import br.com.vaultgame.vaultgame.model.JogoModel;
import br.com.vaultgame.vaultgame.repository.CategoriaRepository;
import br.com.vaultgame.vaultgame.repository.JogoRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class JogoService {
    final JogoMapper jogoMapper;
    final JogoRepository jogoRepository;
    final CategoriaRepository categoriaRepository;

    public JogoService(JogoMapper jogoMapper, JogoRepository jogoRepository, CategoriaRepository categoriaRepository){
        this.jogoMapper = jogoMapper;
        this.jogoRepository = jogoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public JogoResponseDTO criarJogo(JogoRequestDTO jogoRequestDTO){
        JogoModel novoJogo = jogoMapper.toEntity(jogoRequestDTO);
        JogoModel jogoSalvo = jogoRepository.save(novoJogo);
        return jogoMapper.toResponseDTO(jogoSalvo);
    }

    public JogoResponseDTO getJogoById(Long id) {
        JogoModel jogoEncontrado = jogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo", id));
        return jogoMapper.toResponseDTO(jogoEncontrado);
    }

    public List<JogoResponseDTO> getAllJogos() {
        List<JogoModel> listTodosJogos = jogoRepository.findAll();

        return listTodosJogos.stream()
                .map(jogoMapper::toResponseDTO)
                .toList();
    }

    public void deleteJogoById(Long id) {
        if (!jogoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Jogo", id);
        }
        jogoRepository.deleteById(id);
    }

    public JogoResponseDTO updateJogo(Long id, JogoRequestDTO jogoAtualizado) {
        JogoModel jogoExistente = jogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo", id));

        jogoExistente.setTitulo(jogoAtualizado.titulo());
        jogoExistente.setDescricao(jogoAtualizado.descricao());
        jogoExistente.setDataLancamento(jogoAtualizado.dataLancamento());
        jogoExistente.setPreco(jogoAtualizado.preco());

        if (jogoAtualizado.categoriasIds() != null) {
            var categoriasEncontradas = categoriaRepository.findAllById(jogoAtualizado.categoriasIds());
            jogoExistente.setCategorias(new HashSet<>(categoriasEncontradas));
        }

        JogoModel salvo = jogoRepository.save(jogoExistente);
        return jogoMapper.toResponseDTO(salvo);
    }
}
