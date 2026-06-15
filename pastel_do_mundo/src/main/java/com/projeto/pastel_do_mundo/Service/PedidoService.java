package com.projeto.pastel_do_mundo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.Repository.PedidoRepository;
import com.projeto.pastel_do_mundo.dto.pedidoRequestDTO;
import com.projeto.pastel_do_mundo.dto.pedidoResponseDTO;

@Service

public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<pedidoResponseDTO> listarPedido() {
        return pedidoRepository.findAll()
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
    }

    public pedidoResponseDTO fazerPedido(pedidoRequestDTO dto) {
        Pedido pedido = new Pedido();

        pedido.setId(dto.getId());
        pedido.setNome(dto.getNome());
        pedido.setStatus(dto.getStatus());
        pedido.setTotal(dto.getTotal());

        Pedido salvo = pedidoRepository.save(pedido);

        return toResponseDTO(pedido);

    }

    public pedidoResponseDTO acharPorIdPedido(Long id) {
        Pedido pedido =  pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("pedido não localizado"));

        return toResponseDTO(pedido);
    }

    public void deletarPedidoPorID(Long id) {
        pedidoRepository.deleteById(id);
    }

    private pedidoResponseDTO toResponseDTO (Pedido pedido) {
        pedidoResponseDTO dto = new pedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setNome(pedido.getNome());
        dto.setStatus(pedido.getStatus());
        dto.setTotal(pedido.getTotal());

        return dto;
    }


}
