package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RelatorioFinanceiroDTO {

    private PeriodoFinanceiro periodo;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    private int quantidadePedidos;
    private BigDecimal faturamentoBruto;
    private BigDecimal totalTaxas;
    private BigDecimal valorLiquido;
    private BigDecimal ticketMedio;

    private List<RelatorioPorCanalDTO> porCanal;

    public PeriodoFinanceiro getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoFinanceiro periodo) {
        this.periodo = periodo;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
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

    public BigDecimal getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(BigDecimal ticketMedio) {
        this.ticketMedio = ticketMedio;
    }

    public List<RelatorioPorCanalDTO> getPorCanal() {
        return porCanal;
    }

    public void setPorCanal(List<RelatorioPorCanalDTO> porCanal) {
        this.porCanal = porCanal;
    }
}