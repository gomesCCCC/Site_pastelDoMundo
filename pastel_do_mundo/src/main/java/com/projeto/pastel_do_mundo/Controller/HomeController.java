package com.projeto.pastel_do_mundo.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.projeto.pastel_do_mundo.Model.ItemCarrinhoView;
import com.projeto.pastel_do_mundo.Service.CarrinhoService;
import com.projeto.pastel_do_mundo.Service.ProdutoService;
import com.projeto.pastel_do_mundo.dto.produtoResponseDTO;

import jakarta.servlet.http.HttpSession;

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

    List<produtoResponseDTO> produtos = produtoService.listarProduto();

    Map<String, List<produtoResponseDTO>> produtosPorCategoria = produtos.stream()
        .collect(java.util.stream.Collectors.groupingBy(produtoResponseDTO::getCategoria));

    model.addAttribute("produtosPorCategoria", produtosPorCategoria);

    model.addAttribute("categorias", List.of("PASTEL", "DOCE", "BEBIDA"));

    Map<Long, Integer> carrinho = carrinhoService.listarRaw(session);

    List<ItemCarrinhoView> itens = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

        produtoResponseDTO produto = produtoService.buscarProdutoPorId(entry.getKey());
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

    model.addAttribute("carrinho", itens);
    model.addAttribute("total", total);

    int qtdTotal = carrinho.values().stream().mapToInt(Integer::intValue).sum();
    model.addAttribute("qtdCarrinho", qtdTotal);

    return "home";
}

@GetMapping("/perfil")
public String perfil() {
    return "perfil";
}



}