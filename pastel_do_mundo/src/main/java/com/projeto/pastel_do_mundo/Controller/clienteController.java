package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Service.ClienteService;
import com.projeto.pastel_do_mundo.dto.clienteRequestDTO;
import com.projeto.pastel_do_mundo.dto.clienteResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class clienteController {

    private final ClienteService clienteService;

    public clienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<clienteResponseDTO> listarTodos() {
        return clienteService.listarCliente();
    }

    @GetMapping("/{id}")
    public clienteResponseDTO buscar(@PathVariable Long id) {
        return clienteService.buscarClienteId(id);
    }

    @PostMapping
    public clienteResponseDTO Salvar(@Valid @RequestBody clienteRequestDTO dto) {
        return clienteService.cadastrarCliente(dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        clienteService.apagarCliente(id);
    }

}
