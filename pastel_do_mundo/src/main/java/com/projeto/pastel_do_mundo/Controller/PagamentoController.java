package com.projeto.pastel_do_mundo.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

@Controller
public class PagamentoController {

    @GetMapping("/teste-mp")
    public String teste() {

        try {
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id("1")
                    .title("Pastel Teste")
                    .quantity(1)
                    .currencyId("BRL")
                    .unitPrice(new BigDecimal("10.00"))
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(List.of(item))
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            return "redirect:" + preference.getInitPoint();

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}