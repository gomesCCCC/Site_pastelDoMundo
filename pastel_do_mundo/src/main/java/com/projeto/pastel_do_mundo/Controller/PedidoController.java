package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Model.StatusPedido;
import com.projeto.pastel_do_mundo.Service.PedidoService;
import com.projeto.pastel_do_mundo.dto.pedidoRequestDTO;
import com.projeto.pastel_do_mundo.dto.pedidoResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    
    @GetMapping
    public List<pedidoResponseDTO> MostrarPedidos() {
        return pedidoService.listarPedido();
    }

    @GetMapping("/{id}")
    public pedidoResponseDTO localizarPedido(@PathVariable Long id) {
        return pedidoService.acharPorIdPedido(id);
    }



    @PostMapping
    public pedidoResponseDTO cadastrarPedido(@Valid @RequestBody pedidoRequestDTO dto) {
        return pedidoService.fazerPedido(dto);
    }

    @DeleteMapping("/{id}")
    public void excluirPedido(@PathVariable Long id) {
        pedidoService.deletarPedidoPorID(id);
    }

    @PatchMapping("/{id}/status")
public pedidoResponseDTO atualizarStatus(
        @PathVariable Long id,
        @RequestParam StatusPedido status) {
    return pedidoService.atualizarStatus(id, status);
}
}
