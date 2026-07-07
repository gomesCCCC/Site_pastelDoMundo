package com.projeto.pastel_do_mundo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projeto.pastel_do_mundo.Model.MovimentacaoEstoque;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    @Query("""
        SELECT COALESCE(SUM(
            CASE 
                WHEN m.tipo = 'ENTRADA' THEN m.quantidade
                WHEN m.tipo = 'SAIDA' THEN -m.quantidade
                WHEN m.tipo = 'AJUSTE' THEN m.quantidade
                WHEN m.tipo = 'PERDA' THEN -m.quantidade
            END
        ), 0)
        FROM MovimentacaoEstoque m
        WHERE m.produtoId = :produtoId
    """)
    Integer calcularEstoque(@Param("produtoId") Long produtoId);

    @Query("SELECT m FROM MovimentacaoEstoque m ORDER BY m.data DESC")
    List<MovimentacaoEstoque> listarTodasOrdenado();
}