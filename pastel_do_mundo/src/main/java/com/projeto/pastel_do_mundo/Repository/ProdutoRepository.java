package com.projeto.pastel_do_mundo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.pastel_do_mundo.Model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
