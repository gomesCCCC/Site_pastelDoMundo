package com.projeto.pastel_do_mundo.Mapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.projeto.pastel_do_mundo.Model.ItemPedido;
import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.dto.ItemPedidoResponseDTO;
import com.projeto.pastel_do_mundo.dto.pedidoResponseDTO;

@Component
public class PedidoMapper {

        public pedidoResponseDTO toResponseDTO(Pedido pedido) {

    pedidoResponseDTO dto = new pedidoResponseDTO();

    dto.setId(pedido.getId());
    dto.setStatus(pedido.getStatus());
    dto.setTotal(pedido.getTotal());
    dto.setClienteId(pedido.getCliente().getId());
    dto.setNomeCliente(pedido.getCliente().getNome());
    dto.setTelefoneCliente(pedido.getTelefoneCliente());
    dto.setEnderecoEntrega(pedido.getEnderecoEntrega());
    dto.setCepEntrega(pedido.getCepEntrega());
    dto.setDataPedido(pedido.getDataPedido());



    List<ItemPedidoResponseDTO> itens = pedido.getItens()
        .stream()
        .map(this::toItemDTO)
        .toList();

    dto.setItens(itens);

    return dto;
}

    private ItemPedidoResponseDTO toItemDTO(ItemPedido item) {

        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();

        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setNomeProduto(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUni(item.getPrecoUni());

        dto.setSubtotal(
            item.getPrecoUni().multiply(
                BigDecimal.valueOf(item.getQuantidade())
            )
        );

        return dto;
    }
}
