package com.projeto.pastel_do_mundo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class produtoRequestDTO {

    private Long id;

    @NotBlank
    @Size (min=3, max=50)
    private String nome;

    @PositiveOrZero (message= "O preço não deve ter valor negativo")
    private BigDecimal preco;

    private String URLimagem;

    @Size (min=15, message="A descrição deve ter pelo menos 15 letras")
    private String descricao;

    private char tamanho;

    @PositiveOrZero (message="O estoque não pode negativo")
    private int quantidade;

    @NotEmpty (message="Produto precisa de uma categoria")
    private String categoria;

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getURLimagem() {
        return URLimagem;
    }

    public void setURLimagem(String uRLimagem) {
        URLimagem = uRLimagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public char getTamanho() {
        return tamanho;
    }

    public void setTamanho(char tamanho) {
        this.tamanho = tamanho;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    
}
