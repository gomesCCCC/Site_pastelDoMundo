package com.projeto.pastel_do_mundo.Controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.projeto.pastel_do_mundo.Repository.PedidoRepository;
import com.projeto.pastel_do_mundo.Service.PedidoService;

@RestController
@RequestMapping("/webhook/mercadopago")
public class MercadoPagoWebhookController {

    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;

    public MercadoPagoWebhookController(PedidoRepository pedidoRepository,
                                        PedidoService pedidoService
    ) {
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping
public void receberNotificacao(@RequestBody Map<String, Object> payload) {

    try {

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if (data == null || data.get("id") == null) return;

        Long paymentId = Long.valueOf(data.get("id").toString());

        PaymentClient client = new PaymentClient();
        Payment payment = client.get(paymentId);

        if (!"approved".equals(payment.getStatus())) {
            return;
        }

        String ref = payment.getExternalReference();
        if (ref == null || !ref.startsWith("pedido-")) return;

        Long pedidoId = Long.valueOf(ref.replace("pedido-", ""));

        pedidoService.marcarComoPago(pedidoId);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}