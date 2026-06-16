package com.projeto.pastel_do_mundo.dto;

import java.util.List;

public class CheckoutRequestDTO {

    private Long clienteId;
    private List<ItemCheckoutDTO> itens;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemCheckoutDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemCheckoutDTO> itens) {
        this.itens = itens;
    }
    
    
}
