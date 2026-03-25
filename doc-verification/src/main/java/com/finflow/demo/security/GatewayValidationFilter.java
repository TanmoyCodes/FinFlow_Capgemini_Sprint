package com.finflow.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class GatewayValidationFilter extends OncePerRequestFilter {

    private static final String SECRET = "my-secret-key";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        System.out.println("📌 Incoming Request URI: " + uri);

        // =========================
        // ✅ BYPASS FOR FILE UPLOAD
        // =========================
        if (uri.contains("/documents/upload")) {
            System.out.println("✅ Skipping Gateway Validation for Upload");
            filterChain.doFilter(request, response);
            return;
        }

        // =========================
        // 🔍 DEBUG HEADERS
        // =========================
        System.out.println("📦 All Headers:");
        Collections.list(request.getHeaderNames())
                .forEach(header ->
                        System.out.println(header + ": " + request.getHeader(header))
                );

        String gatewaySecret = request.getHeader("X-Gateway-Secret");

        System.out.println("🔑 Gateway Secret: " + gatewaySecret);

        // =========================
        // ✅ VALIDATE GATEWAY
        // =========================
        if (SECRET.equals(gatewaySecret)) {
            filterChain.doFilter(request, response);
            return;
        }

        // =========================
        // ❌ BLOCK REQUEST
        // =========================
        System.out.println("❌ Access Denied - Invalid Gateway Secret");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Access denied: Only gateway requests allowed");
    }
}