package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Model.StatusPedido;
import com.projeto.pastel_do_mundo.Service.PedidoService;
import com.projeto.pastel_do_mundo.dto.pedidoResponseDTO;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<pedidoResponseDTO> listar() {
        return pedidoService.listarPedido();
    }

    @GetMapping("/{id}")
    public pedidoResponseDTO buscar(@PathVariable Long id) {
        return pedidoService.acharPorIdPedido(id);
    }

    @PatchMapping("/{id}/status")
    public pedidoResponseDTO atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {
        return pedidoService.atualizarStatus(id, status);
    }


}
