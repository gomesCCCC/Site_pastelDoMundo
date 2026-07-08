package com.projeto.pastel_do_mundo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pastel_do_mundo.Service.FinanceiroService;
import com.projeto.pastel_do_mundo.dto.PeriodoFinanceiro;
import com.projeto.pastel_do_mundo.dto.RelatorioFinanceiroDTO;

@RestController
@RequestMapping("/financeiro")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @GetMapping("/relatorio")
    public RelatorioFinanceiroDTO relatorio(@RequestParam PeriodoFinanceiro periodo) {
        return financeiroService.gerarRelatorio(periodo);
    }
}