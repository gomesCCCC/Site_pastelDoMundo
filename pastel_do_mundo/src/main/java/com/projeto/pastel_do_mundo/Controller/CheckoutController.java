package com.projeto.pastel_do_mundo.Controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.projeto.pastel_do_mundo.Model.ItemCarrinhoView;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Service.CarrinhoService;
import com.projeto.pastel_do_mundo.Service.ProdutoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;

    public CheckoutController(CarrinhoService carrinhoService,
                              ProdutoService produtoService) {
        this.carrinhoService = carrinhoService;
        this.produtoService = produtoService;
    }

@GetMapping("/checkout")
public String checkout(Model model, HttpSession session) {

       Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);

List<ItemCarrinhoView> itens = new ArrayList<>();
BigDecimal total = BigDecimal.ZERO;

for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

    Produto produto = produtoService.buscarEntityPorId(entry.getKey());
    int qtd = entry.getValue();

    BigDecimal subtotal = produto.getPreco()
            .multiply(BigDecimal.valueOf(qtd));

    ItemCarrinhoView item = new ItemCarrinhoView(
            produto.getId(),
            produto.getNome(),
            produto.getPreco(),
            qtd,
            subtotal
    );

    itens.add(item);
    total = total.add(subtotal);
}

model.addAttribute("itens", itens);
model.addAttribute("total", total);
model.addAttribute("qtdCarrinho", carrinho.values().stream().mapToInt(Integer::intValue).sum());

return "checkout";
}
}