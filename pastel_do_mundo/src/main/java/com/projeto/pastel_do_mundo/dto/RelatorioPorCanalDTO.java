package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;

import com.projeto.pastel_do_mundo.Model.CanalVenda;

public class RelatorioPorCanalDTO {

    private CanalVenda canal;
    private int quantidadePedidos;
    private BigDecimal faturamentoBruto;
    private BigDecimal totalTaxas;
    private BigDecimal valorLiquido;

    public CanalVenda getCanal() {
        return canal;
    }

    public void setCanal(CanalVenda canal) {
        this.canal = canal;
    }

    public int getQuantidadePedidos() {
        return quantidadePedidos;
    }

    public void setQuantidadePedidos(int quantidadePedidos) {
        this.quantidadePedidos = quantidadePedidos;
    }

    public BigDecimal getFaturamentoBruto() {
        return faturamentoBruto;
    }

    public void setFaturamentoBruto(BigDecimal faturamentoBruto) {
        this.faturamentoBruto = faturamentoBruto;
    }

    public BigDecimal getTotalTaxas() {
        return totalTaxas;
    }

    public void setTotalTaxas(BigDecimal totalTaxas) {
        this.totalTaxas = totalTaxas;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }
}