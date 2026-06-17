package com.projeto.pastel_do_mundo.Model;

import java.math.BigDecimal;

public class ItemCarrinhoView {

    private Long produtoId;
    private String nome;
    private java.math.BigDecimal preco;
    private int quantidade;
    private java.math.BigDecimal subtotal;

    public ItemCarrinhoView(Long produtoId, String nome,
                            java.math.BigDecimal preco,
                            int quantidade,
                            java.math.BigDecimal subtotal) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

}
