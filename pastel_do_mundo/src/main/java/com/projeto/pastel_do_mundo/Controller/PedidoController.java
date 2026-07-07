package com.projeto.pastel_do_mundo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Model.Pedido;
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

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<pedidoResponseDTO> criarPedido(
            @PathVariable Long clienteId,
            @RequestBody Map<Long, Integer> carrinho) {

        Pedido pedido = pedidoService.criarPedidoAberto(clienteId, carrinho);
        pedidoResponseDTO dto = pedidoService.acharPorIdPedido(pedido.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<pedidoResponseDTO>> listarPedidosDoCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    @PutMapping("/cliente/{clienteId}/{pedidoId}/cancelar")
    public ResponseEntity<pedidoResponseDTO> cancelarComoCliente(
            @PathVariable Long clienteId,
            @PathVariable Long pedidoId) {

        pedidoService.cancelarPedidoPorId(pedidoId, clienteId);
        return ResponseEntity.ok(pedidoService.acharPorIdPedido(pedidoId));
    }

    @GetMapping
    public ResponseEntity<List<pedidoResponseDTO>> listarTodosPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedido());
    }

    @GetMapping("/{id}")
    public ResponseEntity<pedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.acharPorIdPedido(id));
    }

    @PutMapping("/admin/{id}/pagamento")
    public ResponseEntity<pedidoResponseDTO> confirmarPagamento(@PathVariable Long id) {
        pedidoService.marcarComoPago(id);
        return ResponseEntity.ok(pedidoService.acharPorIdPedido(id));
    }

    @PutMapping("/admin/{id}/cancelar")
    public ResponseEntity<pedidoResponseDTO> cancelarComoAdmin(@PathVariable Long id) {
        pedidoService.cancelarPedidoAdmin(id);
        return ResponseEntity.ok(pedidoService.acharPorIdPedido(id));
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<pedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusPedido status) {

        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }
}