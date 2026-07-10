package com.projeto.pastel_do_mundo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AdminAuthInterceptor())
                .addPathPatterns(
                        "/admin/**",
                        "/pedidos/admin/**",
                        "/financeiro/**"
                )
                .excludePathPatterns(
                        "/admin/login",
                        "/admin/logout"
                );
    }
}