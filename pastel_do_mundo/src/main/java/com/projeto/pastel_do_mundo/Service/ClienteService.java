package com.projeto.pastel_do_mundo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;
import com.projeto.pastel_do_mundo.dto.clienteRequestDTO;
import com.projeto.pastel_do_mundo.dto.clienteResponseDTO;


@Service
public class ClienteService {


    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    
    public List<clienteResponseDTO> listarCliente() {
        return clienteRepository.findAll()
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
    }
    public clienteResponseDTO cadastrarCliente(clienteRequestDTO dto) {
        Cliente cliente = new Cliente();

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setSenha(dto.getSenha());

        Cliente salvo = clienteRepository.save(cliente);

        return toResponseDTO(salvo);
    }
    

    public void apagarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public clienteResponseDTO buscarClienteId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        return toResponseDTO(cliente);
    }

    private clienteResponseDTO toResponseDTO(Cliente cliente) {
        clienteResponseDTO dto = new clienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        return dto;
    }

    public Cliente autenticar(String email, String senha) {

    Cliente cliente = clienteRepository.findByEmail(email)
        .orElse(null);

    if (cliente != null && cliente.getSenha().equals(senha)) {
        return cliente;
    }

    return null;
}
}
