package com.projeto.pastel_do_mundo.integration.mercadopago;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import com.mercadopago.MercadoPagoConfig;

import jakarta.annotation.PostConstruct;

@Configuration
public class MercadoPagoConfigApp {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }
}
