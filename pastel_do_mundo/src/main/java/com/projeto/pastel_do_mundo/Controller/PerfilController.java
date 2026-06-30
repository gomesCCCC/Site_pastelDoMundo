package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;
import com.projeto.pastel_do_mundo.Repository.PedidoRepository;
import com.projeto.pastel_do_mundo.Service.PedidoService;
import com.projeto.pastel_do_mundo.dto.ClienteRequestDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;

    public PerfilController(ClienteRepository clienteRepository,
                            PedidoRepository pedidoRepository,
                            PedidoService pedidoService) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoService = pedidoService;
    }

    @GetMapping
public String perfil(HttpSession session, Model model) {

    Cliente cliente = (Cliente) session.getAttribute("usuario");

    if (cliente == null) {
        return "redirect:/login";
    }

    model.addAttribute("cliente", cliente);

    List<Pedido> pedidos = pedidoRepository.findByClienteId(cliente.getId());
    model.addAttribute("pedidos", pedidos);

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

@PostMapping("/cancelar")
public String cancelar(@RequestParam Long pedidoId, HttpSession session) {

    Cliente cliente = (Cliente) session.getAttribute("usuario");

    pedidoService.cancelarPedidoPorId(pedidoId, cliente.getId());

    return "redirect:/perfil";
}
}