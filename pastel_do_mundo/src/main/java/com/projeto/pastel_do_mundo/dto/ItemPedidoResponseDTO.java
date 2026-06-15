package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;

public class ItemPedidoResponseDTO {

    private Long id;
    private int quantidade;
    private BigDecimal precoUni;
    private BigDecimal subtotal;

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