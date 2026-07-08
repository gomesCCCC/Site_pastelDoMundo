package com.projeto.pastel_do_mundo.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projeto.pastel_do_mundo.Model.CanalVenda;
import com.projeto.pastel_do_mundo.Model.Pedido;
import com.projeto.pastel_do_mundo.Model.StatusPedido;
import com.projeto.pastel_do_mundo.Repository.PedidoRepository;
import com.projeto.pastel_do_mundo.dto.PeriodoFinanceiro;
import com.projeto.pastel_do_mundo.dto.RelatorioFinanceiroDTO;
import com.projeto.pastel_do_mundo.dto.RelatorioPorCanalDTO;

@Service
public class FinanceiroService {

    private final PedidoRepository pedidoRepository;

    public FinanceiroService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public RelatorioFinanceiroDTO gerarRelatorio(PeriodoFinanceiro periodo) {

        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = calcularInicio(periodo, fim);

        List<Pedido> pedidosPagos = pedidoRepository
                .findByStatusAndDataPedidoBetween(StatusPedido.FINALIZADO, inicio, fim);

        RelatorioFinanceiroDTO dto = new RelatorioFinanceiroDTO();
        dto.setPeriodo(periodo);
        dto.setInicio(inicio);
        dto.setFim(fim);
        dto.setQuantidadePedidos(pedidosPagos.size());

        BigDecimal faturamentoBruto = somarBruto(pedidosPagos);
        BigDecimal totalTaxas = somarTaxas(pedidosPagos);
        BigDecimal valorLiquido = faturamentoBruto.subtract(totalTaxas);

        dto.setFaturamentoBruto(faturamentoBruto);
        dto.setTotalTaxas(totalTaxas);
        dto.setValorLiquido(valorLiquido);
        dto.setTicketMedio(calcularTicketMedio(faturamentoBruto, pedidosPagos.size()));
        dto.setPorCanal(agruparPorCanal(pedidosPagos));

        return dto;
    }

    private LocalDateTime calcularInicio(PeriodoFinanceiro periodo, LocalDateTime fim) {
        return switch (periodo) {
            case HOJE -> fim.toLocalDate().atStartOfDay();
            case SEMANA -> fim.minusDays(7);
            case MES -> LocalDate.of(fim.getYear(), fim.getMonth(), 1).atStartOfDay();
        };
    }

    private BigDecimal somarBruto(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal somarTaxas(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(this::calcularTaxaDoPedido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTaxaDoPedido(Pedido pedido) {
        BigDecimal taxaPercentual = pedido.getCanalVenda().getTaxaPercentual();
        return pedido.getTotal().multiply(taxaPercentual).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularTicketMedio(BigDecimal faturamentoBruto, int quantidadePedidos) {
        if (quantidadePedidos == 0) {
            return BigDecimal.ZERO;
        }
        return faturamentoBruto.divide(BigDecimal.valueOf(quantidadePedidos), 2, RoundingMode.HALF_UP);
    }

    private List<RelatorioPorCanalDTO> agruparPorCanal(List<Pedido> pedidos) {

        Map<CanalVenda, List<Pedido>> porCanal = pedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getCanalVenda, () -> new EnumMap<>(CanalVenda.class), Collectors.toList()));

        return porCanal.entrySet().stream()
                .map(entry -> {
                    CanalVenda canal = entry.getKey();
                    List<Pedido> pedidosDoCanal = entry.getValue();

                    RelatorioPorCanalDTO canalDTO = new RelatorioPorCanalDTO();
                    canalDTO.setCanal(canal);
                    canalDTO.setQuantidadePedidos(pedidosDoCanal.size());

                    BigDecimal bruto = somarBruto(pedidosDoCanal);
                    BigDecimal taxas = somarTaxas(pedidosDoCanal);

                    canalDTO.setFaturamentoBruto(bruto);
                    canalDTO.setTotalTaxas(taxas);
                    canalDTO.setValorLiquido(bruto.subtract(taxas));

                    return canalDTO;
                })
                .collect(Collectors.toList());
    }
}