package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;
import java.util.List;

import com.projeto.pastel_do_mundo.Model.StatusPedido;

public class pedidoResponseDTO {

    private Long id;
    private String nomePedido;
    private Long clienteId;
    private String nomeCliente;
    private String nome;
    private StatusPedido status;
    private BigDecimal total;
    private List<ItemPedidoResponseDTO> itens;
    
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
    public StatusPedido getStatus() {
        return status;
    }
    public void setStatus(StatusPedido status) {
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
    public Long getClienteId() {
        return clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoResponseDTO> itens) {
        this.itens = itens;
    }

    
}
