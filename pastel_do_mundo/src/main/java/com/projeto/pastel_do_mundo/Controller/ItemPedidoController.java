package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Service.ItemPedidoService;
import com.projeto.pastel_do_mundo.dto.ItemPedidoRequestDTO;
import com.projeto.pastel_do_mundo.dto.ItemPedidoResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/itens-pedido")
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    public ItemPedidoController(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @PostMapping
    public ItemPedidoResponseDTO criar(@Valid @RequestBody ItemPedidoRequestDTO dto) {
        return itemPedidoService.cadastrar(dto);
    }

    @GetMapping
    public List<ItemPedidoResponseDTO> listar() {
        return itemPedidoService.listar();
    }
}