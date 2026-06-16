package com.projeto.pastel_do_mundo.dto;

public class pedidoRequestDTO {


    private Long clienteId;
    private String status;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
