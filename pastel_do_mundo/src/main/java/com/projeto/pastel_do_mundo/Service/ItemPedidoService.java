package com.projeto.pastel_do_mundo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.ItemPedido;
import com.projeto.pastel_do_mundo.Repository.ItemPedidoRepository;
import com.projeto.pastel_do_mundo.dto.ItemPedidoRequestDTO;
import com.projeto.pastel_do_mundo.dto.ItemPedidoResponseDTO;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public ItemPedidoResponseDTO cadastrar(ItemPedidoRequestDTO dto) {

        ItemPedido item = new ItemPedido();
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUni(dto.getPrecoUni());

        ItemPedido salvo = itemPedidoRepository.save(item);

        return toResponseDTO(salvo);
    }

    public List<ItemPedidoResponseDTO> listar() {
        return itemPedidoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private ItemPedidoResponseDTO toResponseDTO(ItemPedido item) {

        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(item.getId());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUni(item.getPrecoUni());

        return dto;
    }
}
