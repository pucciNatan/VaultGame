package br.com.vaultgame.vaultgame.controller;

import br.com.vaultgame.vaultgame.dto.categorias.CategoriaRequestDTO;
import br.com.vaultgame.vaultgame.dto.categorias.CategoriaResponseDTO;
import br.com.vaultgame.vaultgame.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public CategoriaResponseDTO criarCategoria(@RequestBody @Valid CategoriaRequestDTO categoriaRequestDTO) {
        return categoriaService.criarCategoria(categoriaRequestDTO);
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO getCategoria(@PathVariable Long id) {
        return categoriaService.getCategoriaById(id);
    }

    @GetMapping
    public List<CategoriaResponseDTO> getAllCategorias() {
        return categoriaService.getAllCategorias();
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO updateCategoria(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO categoriaAtualizada) {
        return categoriaService.updateCategoria(id, categoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoriaById(id);
    }
}
