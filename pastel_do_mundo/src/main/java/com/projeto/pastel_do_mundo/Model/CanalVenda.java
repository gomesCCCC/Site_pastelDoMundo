package com.projeto.pastel_do_mundo.Model;

import java.math.BigDecimal;

public enum CanalVenda {

    SITE_PROPRIO(BigDecimal.ZERO),
    IFOOD(new BigDecimal("0.23")); // VALOR DE EXEMPLO //

    private final BigDecimal taxaPercentual;

    CanalVenda(BigDecimal taxaPercentual) {
        this.taxaPercentual = taxaPercentual;
    }

    public BigDecimal getTaxaPercentual() {
        return taxaPercentual;
    }
}