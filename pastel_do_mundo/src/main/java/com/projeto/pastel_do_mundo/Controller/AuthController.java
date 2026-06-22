package com.projeto.pastel_do_mundo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Service.ClienteService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final ClienteService clienteService;

    public AuthController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/login")
public String loginPage() {
    return "login";
}

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        HttpSession session) {

        Cliente cliente = clienteService.autenticar(email, senha);

        if (cliente == null) {
            return "redirect:/login?erro=true";
        }

        session.setAttribute("usuario", cliente);

        return "redirect:/";
    }

    
}
