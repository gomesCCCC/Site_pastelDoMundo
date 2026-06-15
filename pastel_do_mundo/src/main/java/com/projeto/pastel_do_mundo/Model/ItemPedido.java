package com.projeto.pastel_do_mundo.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name= "ITEMPEDIDO")
public class ItemPedido {

    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)

    private Long id;
    private int quantidade;
    private BigDecimal precoUni;

    public ItemPedido() {

    }

    public ItemPedido(Long id, int quantidade, BigDecimal precoUni) {
        this.id = id;
        this.quantidade = quantidade;
        this.precoUni = precoUni;
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
    

}
