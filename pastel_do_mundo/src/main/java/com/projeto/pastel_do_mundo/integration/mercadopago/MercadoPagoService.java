package com.projeto.pastel_do_mundo.integration.mercadopago;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.preference.Preference;
import com.projeto.pastel_do_mundo.Model.Produto;
import com.projeto.pastel_do_mundo.Service.ProdutoService;

@Service
public class MercadoPagoService {

    private final ProdutoService produtoService;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Value("${mercadopago.use-sandbox-init-point:true}")
    private boolean useSandboxInitPoint;

    public MercadoPagoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public String criarPagamento(Map<Long, Integer> carrinho, Long pedidoId) {

    try {
        List<PreferenceItemRequest> items = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : carrinho.entrySet()) {

            Produto produto = produtoService.buscarEntityPorId(entry.getKey());

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(String.valueOf(produto.getId()))
                    .title(produto.getNome())
                    .quantity(entry.getValue())
                    .currencyId("BRL")
                    .unitPrice(produto.getPreco())
                    .build();

            items.add(item);
        }

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(appBaseUrl + "/pagamento/sucesso")
                .pending(appBaseUrl + "/pagamento/pendente")
                .failure(appBaseUrl + "/pagamento/falha")
                .build();

        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                .installments(1)
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .externalReference("pedido-" + pedidoId)
                .notificationUrl(appBaseUrl + "/webhook/mercadopago")
                .paymentMethods(paymentMethods)
                .statementDescriptor("PASTEL DO MUNDO")
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        if (useSandboxInitPoint && preference.getSandboxInitPoint() != null) {
            return preference.getSandboxInitPoint();
        }

        return preference.getInitPoint();

    } catch (MPApiException e) {
        throw new RuntimeException("Erro ao criar pagamento Mercado Pago: "
                + e.getApiResponse().getContent(), e);
    } catch (Exception e) {
        throw new RuntimeException("Erro ao criar pagamento Mercado Pago", e);
    }
}
}
