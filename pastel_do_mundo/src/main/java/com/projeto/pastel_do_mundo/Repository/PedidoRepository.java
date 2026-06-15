package com.projeto.pastel_do_mundo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.pastel_do_mundo.Model.Pedido;

@Repository

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
