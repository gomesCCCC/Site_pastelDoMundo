package com.projeto.pastel_do_mundo.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "PEDIDO")
public class Pedido {

    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)

    private long id;
    private String nome;
    private String status;
    private BigDecimal total;

    public Pedido() {

    }

    public Pedido(Long id,String nome,String status,BigDecimal total) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
}
