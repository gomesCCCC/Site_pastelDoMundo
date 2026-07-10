package com.projeto.pastel_do_mundo.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.projeto.pastel_do_mundo.Repository.AdminRepository;
import com.projeto.pastel_do_mundo.Service.AdminService;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdminService adminService;
    private final AdminRepository adminRepository;

    @Value("${admin.seed.email:}")
    private String email;

    @Value("${admin.seed.senha:}")
    private String senha;

    public AdminSeeder(AdminService adminService,
                       AdminRepository adminRepository) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) {

        if (email.isBlank() || senha.isBlank()) {
            return;
        }

        if (adminRepository.count() == 0) {

            adminService.cadastrar(
                "Administrador",
                email,
                senha
            );

            System.out.println("Admin inicial criado.");
        }
    }
}
