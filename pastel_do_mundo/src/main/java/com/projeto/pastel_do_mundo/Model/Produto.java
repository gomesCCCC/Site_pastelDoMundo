package com.projeto.pastel_do_mundo.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "PRODUTO")
public class Produto {

    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)

    private long id;
    private String nome;
    private BigDecimal preco;
    private String URLimagem;
    private String descricao;
    private char tamanho;
    private int quantidade;

    public Produto() {

    }

    public Produto(Long id,String nome,BigDecimal preco,String URLimagem,String descricao, char tamanho, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.URLimagem = URLimagem;
        this.descricao = descricao;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    
}
