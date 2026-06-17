package com.projeto.pastel_do_mundo.Model;

import java.math.BigDecimal;

public class CarrinhoItem {
    private Long produtoId;
    private String nome;
    private java.math.BigDecimal preco;
    private int quantidade;

    public java.math.BigDecimal getSubtotal() {
        return preco.multiply(new java.math.BigDecimal(quantidade));
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
}
