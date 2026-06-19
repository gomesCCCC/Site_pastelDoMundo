package com.projeto.pastel_do_mundo.dto;

import com.projeto.pastel_do_mundo.Model.StatusPedido;

public class pedidoRequestDTO {

   
    private Long clienteId;
    private StatusPedido status;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

}
