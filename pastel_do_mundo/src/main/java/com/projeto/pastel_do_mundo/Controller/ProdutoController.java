package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Service.ProdutoService;
import com.projeto.pastel_do_mundo.dto.produtoRequestDTO;
import com.projeto.pastel_do_mundo.dto.produtoResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<produtoResponseDTO> ListaProduto() {
        return produtoService.listarProduto();
    }

    @GetMapping("/{id}")
    public produtoResponseDTO acharPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id);
    }

    @PostMapping
    public produtoResponseDTO Salvar(@Valid @RequestBody produtoRequestDTO dto) {
        return produtoService.cadastrarProduto(dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        produtoService.eliminarProduto(id);
    }

    @PatchMapping("/{id}/estoque")
public void atualizarEstoque(@PathVariable Long id, @RequestParam int quantidade) {
    produtoService.atualizarEstoque(id, quantidade);
}  

@PatchMapping("/{id}/disponibilidade")
public produtoResponseDTO alternarDisponibilidade(@PathVariable Long id) {
    return produtoService.alternarDisponibilidade(id);
}

}
