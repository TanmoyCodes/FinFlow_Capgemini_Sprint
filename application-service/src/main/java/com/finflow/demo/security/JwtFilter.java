package com.finflow.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${gateway.secret}")
    private String gatewaySecret;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        System.out.println("➡️ Incoming Request: " + path);

        // ✅ Skip auth endpoints
        if (path.startsWith("/auth") ||
        	    path.startsWith("/application/v3/api-docs") ||
        	    path.contains("/v3/api-docs") ||
        	    path.contains("/swagger-ui") ||
        	    path.contains("/swagger-resources") ||
        	    path.contains("/webjars")) {

        	    System.out.println("✅ Swagger/Auth bypass: " + path);
        	    filterChain.doFilter(request, response);
        	    return;
        	}
        // 🔐 Gateway check
        String secret = request.getHeader("X-Gateway-Secret");

        if (secret == null || !gatewaySecret.equals(secret)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied: Only Gateway allowed");
            return;
        }

        System.out.println("✅ Gateway Secret Passed");

        // 🔑 JWT check
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔐 Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing JWT");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("🎟️ Token Extracted");

        try {
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            System.out.println("👤 Email: " + email);
            System.out.println("🛡️ Role: " + role);

            var authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );

            System.out.println("Authorities: " + authorities);

            UsernamePasswordAuthenticationToken authToken =
            	    new UsernamePasswordAuthenticationToken(email, token, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("✅ Authentication Set in Context");

        } catch (Exception e) {
            System.out.println("❌ JWT FAILED: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}