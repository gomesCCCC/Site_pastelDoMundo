package com.projeto.pastel_do_mundo.Config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminAuthInterceptor implements HandlerInterceptor {

    public AdminAuthInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        boolean logado = session != null && session.getAttribute("admin") != null;

        if (logado) {
            return true;
        }

        String path = request.getRequestURI();

        if (path.startsWith("/admin/")) {
            response.sendRedirect("/admin/login");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\":\"Acesso negado - faça login como admin\"}");
        }

        return false;
    }
}