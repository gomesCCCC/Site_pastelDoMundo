package com.projeto.pastel_do_mundo.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.pastel_do_mundo.Model.ItemCarrinhoView;
import com.projeto.pastel_do_mundo.Service.CarrinhoService;
import com.projeto.pastel_do_mundo.Service.ProdutoService;
import com.projeto.pastel_do_mundo.dto.produtoResponseDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;

    public CarrinhoController(CarrinhoService carrinhoService,
                              ProdutoService produtoService) {
        this.carrinhoService = carrinhoService;
        this.produtoService = produtoService;
    }

    @PostMapping("/adicionar")
    public String adicionar(@RequestParam Long produtoId,
                            HttpSession session) {

        carrinhoService.adicionar(produtoId, session);
        return "redirect:/";
    }

@PostMapping("/remover")
public String remover(@RequestParam Long produtoId, HttpSession session) {
    carrinhoService.remover(produtoId, session);
    return "redirect:/";
}

    @GetMapping
    public String visualizar(Model model, HttpSession session) {

        Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);

        List<ItemCarrinhoView> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

            produtoResponseDTO produto =
                    produtoService.buscarProdutoPorId(entry.getKey());

            int qtd = entry.getValue();

            BigDecimal subtotal =
                    produto.getPreco().multiply(BigDecimal.valueOf(qtd));

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

        return "carrinho";
    }
}