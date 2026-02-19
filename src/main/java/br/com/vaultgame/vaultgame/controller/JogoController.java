package br.com.vaultgame.vaultgame.controller;

import br.com.vaultgame.vaultgame.dto.jogos.JogoRequestDTO;
import br.com.vaultgame.vaultgame.dto.jogos.JogoResponseDTO;
import br.com.vaultgame.vaultgame.service.JogoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {
    private final JogoService jogoService;

    public JogoController(JogoService jogoService){
        this.jogoService = jogoService;
    }

    @PostMapping
    public JogoResponseDTO criarJogo(@RequestBody @Valid JogoRequestDTO jogoRequestDTO) {
        return jogoService.criarJogo(jogoRequestDTO);
    }

    @GetMapping("/{id}")
    public JogoResponseDTO getJogo(@PathVariable Long id) {
        return jogoService.getJogoById(id);
    }

    @GetMapping
    public List<JogoResponseDTO> getAllJogos(){
        return jogoService.getAllJogos();
    }

    @PutMapping("/{id}")
    public JogoResponseDTO updateJogo(@PathVariable Long id, @RequestBody @Valid JogoRequestDTO jogoAtualizado) {
        return jogoService.updateJogo(id, jogoAtualizado);
    }

    @DeleteMapping("/{id}")
    public void deleteJogo(@PathVariable Long id){
        jogoService.deleteJogoById(id);
    }
}
