package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;

public class pedidoResponseDTO {

    private Long id;
     private String nomePedido;
    private String nomeCliente;
    private String nome;
    private String status;
    private BigDecimal total;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
    public String getNomePedido() {
        return nomePedido;
    }
    public void setNomePedido(String nomePedido) {
        this.nomePedido = nomePedido;
    }
    public String getNomeCliente() {
        return nomeCliente;
    }
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

        
}
