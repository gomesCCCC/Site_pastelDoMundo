package com.projeto.pastel_do_mundo.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import com.projeto.pastel_do_mundo.dto.CheckoutRequestDTO;
import com.projeto.pastel_do_mundo.dto.ItemCheckoutDTO;
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
    public pedidoResponseDTO checkout(CheckoutRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
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

        for (ItemCheckoutDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (produto.getQuantidade() < itemDTO.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente");
            }

            produto.setQuantidade(produto.getQuantidade() - itemDTO.getQuantidade());
            produtoRepository.save(produto);

            BigDecimal subtotal =
                    produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));

            total = total.add(subtotal);

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUni(produto.getPreco());

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        Pedido salvo = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(salvo);
    }

    public List<pedidoResponseDTO> listarPedido() {

        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
public pedidoResponseDTO acharPorIdPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return pedidoMapper.toResponseDTO(pedido);
    }

    public pedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(status);

        Pedido atualizado = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(atualizado);
    }

    public void deletarPedidoPorID(Long id) {

        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado");
        }

        pedidoRepository.deleteById(id);
    }
}