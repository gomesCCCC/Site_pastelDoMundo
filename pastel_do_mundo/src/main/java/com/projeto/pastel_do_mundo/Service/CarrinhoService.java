package com.projeto.pastel_do_mundo.Service;

import com.projeto.pastel_do_mundo.dto.produtoResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarrinhoService {

    private static final String CART_KEY = "carrinho";

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCarrinho(HttpSession session) {
        Object carrinho = session.getAttribute(CART_KEY);

        if (carrinho == null) {
            carrinho = new HashMap<Long, Integer>();
            session.setAttribute(CART_KEY, carrinho);
        }

        return (Map<Long, Integer>) carrinho;
    }

    public void adicionar(Long produtoId, HttpSession session) {
        Map<Long, Integer> carrinho = getCarrinho(session);
        carrinho.put(produtoId,
                carrinho.getOrDefault(produtoId, 0) + 1);
    }

    public Map<Long, Integer> listarRaw(HttpSession session) {
        return getCarrinho(session);
    }

    public void remover(Long produtoId, HttpSession session) {
        getCarrinho(session).remove(produtoId);
    }

    public void limpar(HttpSession session) {
        session.removeAttribute(CART_KEY);
    }
}