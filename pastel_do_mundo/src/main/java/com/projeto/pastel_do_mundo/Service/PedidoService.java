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
        pedido.setEstoqueDebitado(false);

        BigDecimal total = BigDecimal.ZERO;
        List<ItemPedido> itens = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

            Produto produto = produtoRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            int quantidade = entry.getValue();

            if (produto.getQuantidade() < quantidade) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getId());
            }

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

    public List<pedidoResponseDTO> listarPorCliente(Long clienteId) {

        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void marcarComoPago(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido cancelado não pode ser marcado como pago");
        }

        if (pedido.isEstoqueDebitado()) {
            pedido.setStatus(StatusPedido.FINALIZADO);
            pedidoRepository.save(pedido);
            return;
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            if (produto.getQuantidade() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getId());
            }
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setEstoqueDebitado(true);
        pedido.setStatus(StatusPedido.FINALIZADO);

        pedidoRepository.save(pedido);
    }

    public pedidoResponseDTO acharPorIdPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return pedidoMapper.toResponseDTO(pedido);
    }

    public pedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {

        if (status == StatusPedido.FINALIZADO) {
            marcarComoPago(id);
            return acharPorIdPedido(id);
        }

        if (status == StatusPedido.CANCELADO) {
            cancelarPedidoAdmin(id);
            return acharPorIdPedido(id);
        }

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(status);

        Pedido atualizado = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public void cancelarPedidoPorId(Long id, Long clienteId) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("Acesso negado");
        }

        cancelarInterno(pedido);
    }

    @Transactional
    public void cancelarPedidoAdmin(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        cancelarInterno(pedido);
    }

    private void cancelarInterno(Pedido pedido) {

        if (pedido.getStatus() == StatusPedido.CANCELADO) return;

        restaurarEstoqueSeNecessario(pedido);

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private void restaurarEstoqueSeNecessario(Pedido pedido) {

        if (!pedido.isEstoqueDebitado()) {
            return;
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setEstoqueDebitado(false);
    }
}