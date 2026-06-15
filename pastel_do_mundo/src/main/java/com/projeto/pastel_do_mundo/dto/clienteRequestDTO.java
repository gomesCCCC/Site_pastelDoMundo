package com.projeto.pastel_do_mundo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class clienteRequestDTO {

    
    private Long id;
    
    @NotBlank
    @Size (min=3)
    private String nome;

    @NotBlank(message= "Não é possível entrar sem email")
    @Email(message = "email inválido")
    private String email;

    @NotBlank
    @Size(min=3)
    private String senha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
