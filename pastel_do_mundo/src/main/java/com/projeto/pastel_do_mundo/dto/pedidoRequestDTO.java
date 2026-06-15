package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class pedidoRequestDTO {

    private Long id;
    
    @NotBlank (message = "Pedido precisa de um cliente")
    private String nome;

    @NotBlank(message = "é necessário fornece status")
    private String status;

    @PositiveOrZero(message= "precisa ser maior ou igual a 0")
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


}
