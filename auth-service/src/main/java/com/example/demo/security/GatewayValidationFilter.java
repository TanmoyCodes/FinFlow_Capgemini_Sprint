package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class GatewayValidationFilter extends OncePerRequestFilter {

    @Value("${gateway.secret}")
    private String gatewaySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        String secret = request.getHeader("X-Gateway-Secret");

        // =========================
        // ✅ VALIDATE GATEWAY
        // =========================
        if (gatewaySecret.equals(secret)) {
            filterChain.doFilter(request, response);
            return;
        }

        // =========================
        // ❌ BLOCK REQUEST
        // =========================
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Access denied: Only gateway requests allowed");
    }
}
