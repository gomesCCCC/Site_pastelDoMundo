package com.projeto.pastel_do_mundo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Model.TipoMovimentacao;
import com.projeto.pastel_do_mundo.Repository.ProdutoRepository;
import com.projeto.pastel_do_mundo.dto.produtoRequestDTO;
import com.projeto.pastel_do_mundo.dto.produtoResponseDTO;

@Service

public class ProdutoService {

private final MovimentacaoEstoqueService movimentacaoEstoqueService;
private final ProdutoRepository produtoRepository;

public ProdutoService(ProdutoRepository produtoRepository,
                      MovimentacaoEstoqueService movimentacaoEstoqueService) {
    this.produtoRepository = produtoRepository;
    this.movimentacaoEstoqueService = movimentacaoEstoqueService;
}

@Transactional
public void atualizarEstoque(Long id, int quantidade, String motivo) {
    movimentacaoEstoqueService.registrar(id, quantidade, TipoMovimentacao.ENTRADA, motivo);
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

@Transactional
public produtoResponseDTO cadastrarProduto(produtoRequestDTO dto) {
    Produto produto = new Produto();

    produto.setNome(dto.getNome());
    produto.setPreco(dto.getPreco());
    produto.setURLimagem(dto.getURLimagem());
    produto.setQuantidade(dto.getQuantidade());
    produto.setTamanho(dto.getTamanho());
    produto.setDescricao(dto.getDescricao());
    produto.setCategoria(dto.getCategoria());
    produto.setAtivo(true);

    Produto salvo = produtoRepository.save(produto);

    movimentacaoEstoqueService.registrarEvento(
        salvo.getId(), salvo.getNome(), TipoMovimentacao.CRIACAO, "Produto cadastrado"
    );

    return toResponseDTO(salvo);
}

@Transactional
public produtoResponseDTO alternarDisponibilidade(Long id, String motivo) {
    Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

    boolean novoStatus = !produto.isAtivo();
    produto.setAtivo(novoStatus);

    Produto salvo = produtoRepository.save(produto);

    movimentacaoEstoqueService.registrarEvento(
        salvo.getId(),
        salvo.getNome(),
        novoStatus ? TipoMovimentacao.REATIVACAO : TipoMovimentacao.DESATIVACAO,
        motivo
    );

    return toResponseDTO(salvo);
}

@Transactional
public void eliminarProduto(Long id, String motivo) {
    Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

    movimentacaoEstoqueService.registrarEvento(
        produto.getId(), produto.getNome(), TipoMovimentacao.REMOCAO, motivo
    );

    produtoRepository.deleteById(id);
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
    dto.setAtivo(produto.isAtivo());
    return dto;
}

public void atualizarEstoque(Long id, int quantidade) {
    Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

    produto.setQuantidade(produto.getQuantidade() + quantidade);

    produtoRepository.save(produto);
}

public produtoResponseDTO alternarDisponibilidade(Long id) {
    Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

    produto.setAtivo(!produto.isAtivo());

    Produto salvo = produtoRepository.save(produto);
    return toResponseDTO(salvo);
}

public List<produtoResponseDTO> listarProdutosDisponiveis() {
    return produtoRepository.findByAtivoTrue()
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
}
    
}
