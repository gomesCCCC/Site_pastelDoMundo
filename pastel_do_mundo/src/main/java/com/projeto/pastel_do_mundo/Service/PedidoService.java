package com.projeto.pastel_do_mundo.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Mapper.PedidoMapper;
import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Model.ItemPedido;
import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Model.StatusPedido;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;
import com.projeto.pastel_do_mundo.Repository.PedidoRepository;
import com.projeto.pastel_do_mundo.Repository.ProdutoRepository;
import com.projeto.pastel_do_mundo.dto.pedidoResponseDTO;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoRepository produtoRepository,
                         PedidoMapper pedidoMapper) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoMapper = pedidoMapper;
    }

@Transactional
public Pedido criarPedidoAberto(Long clienteId, Map<Long, Integer> carrinho) {

    Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

    Pedido pedido = new Pedido();

    pedido.setCliente(cliente);
    pedido.setNomeCliente(cliente.getNome());
    pedido.setTelefoneCliente(cliente.getTelefone());
    pedido.setEnderecoEntrega(cliente.getEndereco());
    pedido.setCepEntrega(cliente.getCEP());

    pedido.setStatus(StatusPedido.ABERTO);

    BigDecimal total = BigDecimal.ZERO;
    List<ItemPedido> itens = new ArrayList<>();

    for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

        Produto produto = produtoRepository.findById(entry.getKey())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int quantidade = entry.getValue();

        if (produto.getQuantidade() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        produto.setQuantidade(produto.getQuantidade() - quantidade);
        produtoRepository.save(produto);

        BigDecimal subtotal =
                produto.getPreco().multiply(BigDecimal.valueOf(quantidade));

        total = total.add(subtotal);

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUni(produto.getPreco());

        itens.add(item);
    }

    pedido.setItens(itens);
    pedido.setTotal(total);

    return pedidoRepository.save(pedido);
}

    public List<pedidoResponseDTO> listarPedido() {

        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

@Transactional
public void marcarComoPago(Long pedidoId) {

    Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

    pedido.setStatus(StatusPedido.FINALIZADO);

    pedidoRepository.save(pedido);
}

public pedidoResponseDTO acharPorIdPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return pedidoMapper.toResponseDTO(pedido);
    }

    public pedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {

    Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

    if (!podeAlterarStatus(pedido.getStatus(), status)) {
        throw new RuntimeException(
            "Não é possível alterar pedido de "
            + pedido.getStatus()
            + " para "
            + status
        );
    }

    pedido.setStatus(status);

    Pedido atualizado = pedidoRepository.save(pedido);

    return pedidoMapper.toResponseDTO(atualizado);
}


    private boolean podeAlterarStatus(
        StatusPedido atual,
        StatusPedido novo
) {

    return switch (atual) {

        case ABERTO ->
                novo == StatusPedido.PROCESSANDO ||
                novo == StatusPedido.CANCELADO;

        case PROCESSANDO ->
                novo == StatusPedido.FINALIZADO;

        case FINALIZADO, CANCELADO ->
                false;
    };
}

public void cancelarPedidoPorId(Long id, Long clienteId) {

    Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

    if (!pedido.getCliente().getId().equals(clienteId)) {
        throw new RuntimeException("Acesso negado");
    }

    if (pedido.getStatus() == StatusPedido.CANCELADO) return;

    pedido.setStatus(StatusPedido.CANCELADO);
    pedidoRepository.save(pedido);
}



    
}