package com.projeto.pastel_do_mundo.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ConfiguracaoFinanceira {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private BigDecimal percentualImposto;


    private BigDecimal taxaIfood;


    private BigDecimal outrasTaxas;



    public Long getId() {
        return id;
    }



    public BigDecimal getPercentualImposto() {
        return percentualImposto;
    }


    public void setPercentualImposto(BigDecimal percentualImposto) {
        this.percentualImposto = percentualImposto;
    }



    public BigDecimal getTaxaIfood() {
        return taxaIfood;
    }


    public void setTaxaIfood(BigDecimal taxaIfood) {
        this.taxaIfood = taxaIfood;
    }



    public BigDecimal getOutrasTaxas() {
        return outrasTaxas;
    }


    public void setOutrasTaxas(BigDecimal outrasTaxas) {
        this.outrasTaxas = outrasTaxas;
    }

}