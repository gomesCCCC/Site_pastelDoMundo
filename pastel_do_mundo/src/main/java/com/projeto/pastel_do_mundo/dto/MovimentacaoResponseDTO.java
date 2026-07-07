package com.projeto.pastel_do_mundo.dto;

import java.time.LocalDateTime;

public class MovimentacaoResponseDTO {

    private Long id;
    private String produtoNome;
    private Integer quantidade;
    private String tipo;
    private LocalDateTime data;
    private String motivo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
