package com.projeto.pastel_do_mundo.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CarrinhoService {

    private static final String CARRINHO = "carrinho";

    @SuppressWarnings("unchecked")
    public Map<Long, Integer> listarRaw(HttpSession session) {

        Map<Long, Integer> carrinho =
                (Map<Long, Integer>) session.getAttribute(CARRINHO);

        if (carrinho == null) {
            carrinho = new HashMap<>();
            session.setAttribute(CARRINHO, carrinho);
        }

        return carrinho;
    }

    public void adicionar(Long produtoId, HttpSession session) {

        Map<Long, Integer> carrinho = listarRaw(session);

        carrinho.put(
                produtoId,
                carrinho.getOrDefault(produtoId, 0) + 1
        );

        session.setAttribute(CARRINHO, carrinho);
    }

    public void remover(Long produtoId, HttpSession session) {

        Map<Long, Integer> carrinho = listarRaw(session);

        carrinho.remove(produtoId);

        session.setAttribute(CARRINHO, carrinho);
    }

    public void limpar(HttpSession session) {

        session.removeAttribute(CARRINHO);
    }
}