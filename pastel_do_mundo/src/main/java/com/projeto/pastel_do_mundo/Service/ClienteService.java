package com.projeto.pastel_do_mundo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;


@Service
public class ClienteService {


    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarCliente() {
        return clienteRepository.findAll();
    }
    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void apagarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente buscarClienteId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("cliente não encontrado"));
    }
}
