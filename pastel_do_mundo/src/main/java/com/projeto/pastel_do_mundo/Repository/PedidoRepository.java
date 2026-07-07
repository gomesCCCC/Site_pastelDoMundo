package com.projeto.pastel_do_mundo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.pastel_do_mundo.Model.Pedido;

@Repository

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

     List<Pedido> findByClienteId(Long clienteId);

     List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

     List<Pedido> findAllByOrderByDataPedidoDesc();

}
