package com.projeto.pastel_do_mundo.Service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.pastel_do_mundo.Model.MovimentacaoEstoque;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Model.TipoMovimentacao;
import com.projeto.pastel_do_mundo.Repository.MovimentacaoEstoqueRepository;
import com.projeto.pastel_do_mundo.Repository.ProdutoRepository;

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
    public void registrar(Long produtoId,
                          Integer quantidade,
                          TipoMovimentacao tipo,
                          String motivo) {

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProduto(produto);
        mov.setQuantidade(quantidade);
        mov.setTipo(tipo);
        mov.setData(LocalDateTime.now());
        mov.setMotivo(motivo);

        repository.save(mov);
    }

    public Integer getEstoqueAtual(Long produtoId) {
        return repository.calcularEstoque(produtoId);
    }
}
