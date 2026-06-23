package com.projeto.pastel_do_mundo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;
import com.projeto.pastel_do_mundo.Service.ClienteService;
import com.projeto.pastel_do_mundo.dto.ClienteRequestDTO;
import com.projeto.pastel_do_mundo.dto.ClienteResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class clienteController {

    private final ClienteService clienteService;
     private final ClienteRepository clienteRepository;

    public clienteController(ClienteService clienteService,ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<ClienteResponseDTO> listarTodos() {
        return clienteService.listarCliente();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscar(@PathVariable Long id) {
        return clienteService.buscarClienteId(id);
    }

   @PostMapping
public Cliente salvar(@Valid @RequestBody ClienteRequestDTO dto) {
    return clienteService.cadastrarCliente(dto);
}


}
