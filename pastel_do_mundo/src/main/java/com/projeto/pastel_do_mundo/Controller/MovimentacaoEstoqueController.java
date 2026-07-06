package com.projeto.pastel_do_mundo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Service.MovimentacaoEstoqueService;
import com.projeto.pastel_do_mundo.dto.MovimentacaoRequest;

@RestController
@RequestMapping("/estoque")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService service;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @PostMapping("/movimentar")
    public void movimentar(@RequestBody MovimentacaoRequest request) {
        service.registrar(
                request.getProdutoId(),
                request.getQuantidade(),
                request.getTipo(),
                request.getMotivo()
        );
    }

    @GetMapping("/{produtoId}")
    public Integer estoque(@PathVariable Long produtoId) {
        return service.getEstoqueAtual(produtoId);
    }
}
