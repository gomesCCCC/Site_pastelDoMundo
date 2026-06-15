package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class clienteController {

    private final ClienteService clienteService;

    public clienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarCliente();
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return clienteService.buscarClienteId(id);
    }

    @PostMapping
    public Cliente Salvar(@RequestBody Cliente cliente) {
        return clienteService.cadastrarCliente(cliente);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        clienteService.apagarCliente(id);
    }

}
