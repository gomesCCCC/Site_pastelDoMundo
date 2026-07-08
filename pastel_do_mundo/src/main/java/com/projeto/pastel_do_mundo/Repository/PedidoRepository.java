package com.projeto.pastel_do_mundo.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.Model.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

    List<Pedido> findAllByOrderByDataPedidoDesc();

    List<Pedido> findByStatusAndDataPedidoBetween(
            StatusPedido status,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}