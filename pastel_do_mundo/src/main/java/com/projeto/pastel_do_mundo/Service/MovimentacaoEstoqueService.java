package com.projeto.pastel_do_mundo.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.pastel_do_mundo.Model.MovimentacaoEstoque;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Model.TipoMovimentacao;
import com.projeto.pastel_do_mundo.Repository.MovimentacaoEstoqueRepository;
import com.projeto.pastel_do_mundo.Repository.ProdutoRepository;
import com.projeto.pastel_do_mundo.dto.MovimentacaoResponseDTO;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository repository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository repository,
                                      ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public void registrar(Long produtoId, Integer quantidade, TipoMovimentacao tipo, String motivo) {

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int delta = switch (tipo) {
            case ENTRADA, AJUSTE -> quantidade;
            case SAIDA, PERDA -> -quantidade;
            default -> throw new IllegalArgumentException("Tipo inválido para movimentação de estoque: " + tipo);
        };

        produto.setQuantidade(produto.getQuantidade() + delta);
        produtoRepository.save(produto);

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProdutoId(produto.getId());
        mov.setProdutoNome(produto.getNome());
        mov.setQuantidade(quantidade);
        mov.setTipo(tipo);
        mov.setData(LocalDateTime.now());
        mov.setMotivo(normalizarMotivo(motivo));

        repository.save(mov);
    }

    @Transactional
    public void registrarEvento(Long produtoId, String produtoNome, TipoMovimentacao tipo, String motivo) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProdutoId(produtoId);
        mov.setProdutoNome(produtoNome);
        mov.setQuantidade(0);
        mov.setTipo(tipo);
        mov.setData(LocalDateTime.now());
        mov.setMotivo(normalizarMotivo(motivo));

        repository.save(mov);
    }

    public Integer getEstoqueAtual(Long produtoId) {
        return repository.calcularEstoque(produtoId);
    }

    public List<MovimentacaoResponseDTO> listarHistorico() {
        return repository.listarTodasOrdenado()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MovimentacaoResponseDTO toDTO(MovimentacaoEstoque m) {
        MovimentacaoResponseDTO dto = new MovimentacaoResponseDTO();
        dto.setId(m.getId());
        dto.setProdutoNome(m.getProdutoNome());
        dto.setQuantidade(m.getQuantidade());
        dto.setTipo(m.getTipo().name());
        dto.setData(m.getData());
        dto.setMotivo(m.getMotivo());
        return dto;
    }

    private String normalizarMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            return null;
        }

        return motivo.trim();
    }
}
