package com.projeto.pastel_do_mundo.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.Admin;
import com.projeto.pastel_do_mundo.Repository.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin autenticar(String email, String senha) {

        return adminRepository.findByEmail(email)
                .filter(Admin::isAtivo)
                .filter(admin -> passwordEncoder.matches(senha, admin.getSenha()))
                .orElse(null);
    }

    public Admin cadastrar(String nome, String email, String senhaPura) {

        adminRepository.findByEmail(email).ifPresent(a -> {
            throw new RuntimeException("Já existe um admin cadastrado com esse email");
        });

        Admin admin = new Admin();
        admin.setNome(nome);
        admin.setEmail(email);
        admin.setSenha(passwordEncoder.encode(senhaPura));
        admin.setAtivo(true);

        return adminRepository.save(admin);
    }
}