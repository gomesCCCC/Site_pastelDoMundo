package com.projeto.pastel_do_mundo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.pastel_do_mundo.Model.Admin;
import com.projeto.pastel_do_mundo.Service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminAuthController {

    private final AdminService adminService;

    public AdminAuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/admin-login";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String email,
                         @RequestParam String senha,
                         HttpSession session) {

        Admin admin = adminService.autenticar(email, senha);

        if (admin == null) {
            return "redirect:/admin/login?erro=true";
        }

        session.setAttribute("admin", admin);

        return "redirect:/admin/erp";
    }

    @PostMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/admin/login";
    }
}