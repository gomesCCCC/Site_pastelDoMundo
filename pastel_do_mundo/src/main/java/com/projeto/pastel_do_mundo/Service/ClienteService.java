package com.projeto.pastel_do_mundo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.Cliente;
import com.projeto.pastel_do_mundo.Repository.ClienteRepository;
import com.projeto.pastel_do_mundo.dto.ClienteRequestDTO;
import com.projeto.pastel_do_mundo.dto.ClienteResponseDTO;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder encoder;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public List<ClienteResponseDTO> listarCliente() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Cliente cadastrarCliente(ClienteRequestDTO dto) {

    Cliente cliente = new Cliente();

    cliente.setNome(dto.getNome());
    cliente.setEmail(dto.getEmail());

    cliente.setSenha(
            encoder.encode(dto.getSenha())
    );

    cliente.setEndereco(dto.getEndereco());
    cliente.setTelefone(dto.getTelefone());
    cliente.setCEP(dto.getCep());

    return clienteRepository.save(cliente);
}

    public void apagarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public ClienteResponseDTO buscarClienteId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        return toResponseDTO(cliente);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        return dto;
    }

    public Cliente autenticar(String email, String senha) {

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElse(null);

        if (cliente != null && encoder.matches(senha, cliente.getSenha())) {
            return cliente;
        }

        return null;
    }
}