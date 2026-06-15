package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemPedidoRequestDTO {

    @Min(1)
    private int quantidade;

    @NotNull
    private BigDecimal precoUni;

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
}