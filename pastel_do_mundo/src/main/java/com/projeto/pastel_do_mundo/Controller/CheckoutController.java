package com.projeto.pastel_do_mundo.Controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Model.ItemCarrinhoView;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Service.PedidoService;
import com.projeto.pastel_do_mundo.Service.CarrinhoService;
import com.projeto.pastel_do_mundo.Service.ProdutoService;
import com.projeto.pastel_do_mundo.dto.CheckoutRequestDTO;
import com.projeto.pastel_do_mundo.dto.ItemCheckoutDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public CheckoutController(CarrinhoService carrinhoService,
                              ProdutoService produtoService,
                              PedidoService pedidoService) {
        this.carrinhoService = carrinhoService;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

@GetMapping("/checkout")
public String checkout(Model model, HttpSession session) {

    Cliente cliente = (Cliente) session.getAttribute("usuario");

    if (cliente == null) {
        return "redirect:/login";
    }

    model.addAttribute("cliente", cliente);

    Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);

    List<ItemCarrinhoView> itens = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

        Produto produto = produtoService.buscarEntityPorId(entry.getKey());
        int qtd = entry.getValue();

        BigDecimal subtotal = produto.getPreco()
                .multiply(BigDecimal.valueOf(qtd));

        itens.add(new ItemCarrinhoView(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                qtd,
                subtotal
        ));

        total = total.add(subtotal);
    }

    model.addAttribute("itens", itens);
    model.addAttribute("total", total);
    model.addAttribute("qtdCarrinho",
            carrinho.values().stream().mapToInt(Integer::intValue).sum());

    return "checkout";
}

@PostMapping("/pedidos/confirmar")
public String confirmarPedido(HttpSession session) {

    Cliente cliente = (Cliente) session.getAttribute("usuario");

    if (cliente == null) {
        return "redirect:/login";
    }

    Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);

    if (carrinho.isEmpty()) {
        return "redirect:/checkout";
    }

    CheckoutRequestDTO dto = new CheckoutRequestDTO();
    dto.setClienteId(cliente.getId());

    List<ItemCheckoutDTO> itens = new ArrayList<>();

    for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

        ItemCheckoutDTO item = new ItemCheckoutDTO();
        item.setProdutoId(entry.getKey());
        item.setQuantidade(entry.getValue());

        itens.add(item);
    }

    dto.setItens(itens);

    pedidoService.checkout(dto);

    carrinhoService.limpar(session);

    return "redirect:/pedido/sucesso";
}
}