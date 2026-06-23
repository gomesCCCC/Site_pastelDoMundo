package com.projeto.pastel_do_mundo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;
import com.projeto.pastel_do_mundo.dto.ClienteRequestDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final ClienteRepository clienteRepository;

    public PerfilController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public String perfil(HttpSession session, Model model) {

        Cliente cliente = (Cliente) session.getAttribute("usuario");

        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("cliente", cliente);

        return "perfil";
    }

    @PostMapping("/localizacao")
    public String atualizarLocalizacao(@ModelAttribute ClienteRequestDTO dto,
                                       HttpSession session) {

        Cliente cliente = (Cliente) session.getAttribute("usuario");

        if (cliente == null) {
            return "redirect:/login";
        }

        cliente.setCEP(dto.getCep());
        cliente.setEndereco(dto.getEndereco());
        cliente.setTelefone(dto.getTelefone());

        clienteRepository.save(cliente);

        session.setAttribute("usuario", cliente);

        return "redirect:/perfil";
    }
}