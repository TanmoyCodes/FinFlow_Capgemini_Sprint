package com.finflow.demo.security;

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

        String forwardedPrefix = request.getHeader("X-Forwarded-Prefix");
        System.out.println("🔁 Forwarded Prefix: " + forwardedPrefix);
        System.out.println("📌 Incoming Request URI: " + uri);

        // =========================
        // ✅ BYPASS FOR SWAGGER + UPLOAD
        // =========================
     if (uri.contains("/v3/api-docs") ||
         uri.contains("/swagger-ui") ||
         uri.contains("/swagger-resources") ||
         uri.contains("/webjars")) {

         System.out.println("✅ Skipping Gateway Validation for Swagger/Upload");
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

        String secret = request.getHeader("X-Gateway-Secret");

        System.out.println("🔑 Gateway Secret: " + secret);

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
        System.out.println("❌ Access Denied - Invalid Gateway Secret");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Access denied: Only gateway requests allowed");
    }
}