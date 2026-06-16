package com.projeto.pastel_do_mundo.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Model.ItemPedido;
import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;
import com.projeto.pastel_do_mundo.Repository.PedidoRepository;
import com.projeto.pastel_do_mundo.Repository.ProdutoRepository;
import com.projeto.pastel_do_mundo.dto.CheckoutRequestDTO;
import com.projeto.pastel_do_mundo.dto.ItemCheckoutDTO;
import com.projeto.pastel_do_mundo.dto.pedidoRequestDTO;
import com.projeto.pastel_do_mundo.dto.pedidoResponseDTO;

@Service

public class PedidoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<pedidoResponseDTO> listarPedido() {
        return pedidoRepository.findAll()
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
    }

public pedidoResponseDTO fazerPedido(pedidoRequestDTO dto) {

    Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

    Pedido pedido = new Pedido();

    pedido.setCliente(cliente);
    pedido.setStatus(dto.getStatus());

    pedido.setTotal(BigDecimal.ZERO);

    Pedido salvo = pedidoRepository.save(pedido);

    return toResponseDTO(salvo);
}

    public pedidoResponseDTO acharPorIdPedido(Long id) {
        Pedido pedido =  pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("pedido não localizado"));

        return toResponseDTO(pedido);
    }

    public void deletarPedidoPorID(Long id) {
        pedidoRepository.deleteById(id);
    }

    private pedidoResponseDTO toResponseDTO(Pedido pedido) {
    pedidoResponseDTO dto = new pedidoResponseDTO();

    dto.setId(pedido.getId());
    dto.setNomePedido(pedido.getNome());
    dto.setStatus(pedido.getStatus());
    dto.setTotal(pedido.getTotal());

    dto.setNomeCliente(pedido.getCliente().getNome());

    return dto;
    }

        public pedidoResponseDTO checkout(CheckoutRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus("ABERTO");

        BigDecimal total = BigDecimal.ZERO;

        List<ItemPedido> itens = new ArrayList<>();

        for (ItemCheckoutDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUni(produto.getPreco());

            total = total.add(produto.getPreco()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        Pedido salvo = pedidoRepository.save(pedido);

        return toResponseDTO(salvo);
    }


}
