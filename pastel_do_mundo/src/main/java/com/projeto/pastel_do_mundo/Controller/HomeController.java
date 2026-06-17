package com.projeto.pastel_do_mundo.Controller;

import com.projeto.pastel_do_mundo.Service.CarrinhoService;
import com.projeto.pastel_do_mundo.Service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HomeController {

    private final ProdutoService produtoService;
    private final CarrinhoService carrinhoService;

    public HomeController(ProdutoService produtoService,
                          CarrinhoService carrinhoService) {
        this.produtoService = produtoService;
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        model.addAttribute("produtos", produtoService.listarProduto());

        model.addAttribute("categorias",
                java.util.List.of("Pastéis", "Bebidas", "Combos"));

        Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);

        int qtdTotal = carrinho.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        model.addAttribute("qtdCarrinho", qtdTotal);

        return "home";
    }
}