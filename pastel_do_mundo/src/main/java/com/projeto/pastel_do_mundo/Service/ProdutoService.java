package com.projeto.pastel_do_mundo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Repository.ProdutoRepository;
import com.projeto.pastel_do_mundo.dto.produtoRequestDTO;
import com.projeto.pastel_do_mundo.dto.produtoResponseDTO;

@Service

public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<produtoResponseDTO> listarProduto() {
        return produtoRepository.findAll()
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
    }

    public Produto buscarEntityPorId(Long id) {
    return produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
}

    public produtoResponseDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return toResponseDTO(produto);
    }

    public produtoResponseDTO cadastrarProduto(produtoRequestDTO dto) {
        Produto produto = new Produto();

        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setURLimagem(dto.getURLimagem());
        produto.setTamanho(dto.getTamanho());
        produto.setQuantidade(dto.getQuantidade());
        produto.setDescricao(dto.getDescricao());
        produto.setCategoria(dto.getCategoria());

        Produto salvo = produtoRepository.save(produto);

        return toResponseDTO(salvo);
    }

    public List<produtoResponseDTO> listarPorCategoria(String categoria) {
    return produtoRepository.findAll()
        .stream()
        .filter(p -> categoria.equals(p.getCategoria()))
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
}

    public void eliminarProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    private produtoResponseDTO toResponseDTO(Produto produto) {
        produtoResponseDTO dto = new produtoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        dto.setURLimagem(produto.getURLimagem());
        dto.setQuantidade(produto.getQuantidade());
        dto.setTamanho(produto.getTamanho());
        dto.setDescricao(produto.getDescricao());
        dto.setCategoria(produto.getCategoria());
        return dto;
    }

    
}
