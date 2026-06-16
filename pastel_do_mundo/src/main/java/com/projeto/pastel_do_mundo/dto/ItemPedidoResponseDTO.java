package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;

public class ItemPedidoResponseDTO {

    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private int quantidade;
    private BigDecimal precoUni;
    private BigDecimal subtotal;


    
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUni() {
        return precoUni;
    }

    public void setPrecoUni(BigDecimal precoUni) {
        this.precoUni = precoUni;
    }

    public BigDecimal getSubtotal() {
        return precoUni.multiply(new BigDecimal(quantidade));
    }
}