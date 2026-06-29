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
import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Service.CarrinhoService;
import com.projeto.pastel_do_mundo.Service.PedidoService;
import com.projeto.pastel_do_mundo.Service.ProdutoService;
import com.projeto.pastel_do_mundo.integration.mercadopago.MercadoPagoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;
    private final MercadoPagoService mercadoPagoService;
    private final PedidoService pedidoService;

    public CheckoutController(CarrinhoService carrinhoService,
                              ProdutoService produtoService,
                              MercadoPagoService mercadoPagoService,
                              PedidoService pedidoService) {
        this.carrinhoService = carrinhoService;
        this.produtoService = produtoService;
        this.mercadoPagoService = mercadoPagoService;
        this.pedidoService = pedidoService;
    }

    

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {

        Cliente cliente = (Cliente) session.getAttribute("usuario");

        if (cliente == null) {
            return "redirect:/login";
        }

        Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);
        List<ItemCarrinhoView> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {
            Produto produto = produtoService.buscarEntityPorId(entry.getKey());
            int quantidade = entry.getValue();
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));

            itens.add(new ItemCarrinhoView(
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    quantidade,
                    subtotal
            ));

            total = total.add(subtotal);
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("itens", itens);
        model.addAttribute("total", total);
        model.addAttribute("qtdCarrinho", carrinho.values().stream().mapToInt(Integer::intValue).sum());

        return "checkout";
    }

    @PostMapping("/pedidos/confirmar")
public String confirmarPedido(HttpSession session) {

    Cliente cliente = (Cliente) session.getAttribute("usuario");

    if (cliente == null) return "redirect:/login";

    Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);

    if (carrinho.isEmpty()) return "redirect:/checkout";

Pedido pedido = pedidoService.criarPedidoAberto(cliente.getId(), carrinho);

String url = mercadoPagoService.criarPagamento(carrinho, pedido.getId());

    return "redirect:" + url;
}

    @GetMapping("/pagamento/sucesso")
    public String pagamentoSucesso(HttpSession session) {
        carrinhoService.limpar(session);
        return "redirect:/perfil";
    }

    @GetMapping("/pagamento/pendente")
    public String pagamentoPendente() {
        return "redirect:/checkout";
    }

    @GetMapping("/pagamento/falha")
    public String pagamentoFalha() {
        return "redirect:/checkout";
    }
}
