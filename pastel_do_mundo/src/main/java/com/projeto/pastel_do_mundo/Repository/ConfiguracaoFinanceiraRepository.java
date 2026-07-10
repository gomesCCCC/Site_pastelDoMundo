package com.projeto.pastel_do_mundo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.pastel_do_mundo.Model.ConfiguracaoFinanceira;

@Repository
public interface ConfiguracaoFinanceiraRepository 
        extends JpaRepository<ConfiguracaoFinanceira, Long> {

}