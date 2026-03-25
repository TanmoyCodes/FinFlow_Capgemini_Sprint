package com.finflow.demo.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import com.finflow.demo.util.JwtUtil;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtGatewayFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // ✅ Allow auth endpoints
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        List<String> headers = exchange.getRequest()
                .getHeaders()
                .get(HttpHeaders.AUTHORIZATION);

        if (headers == null || headers.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String authHeader = headers.get(0);

        if (!authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            // ✅ Validate token
            jwtUtil.validateToken(token);

            // ✅ Extract details (SAFE enhancement)
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            return chain.filter(
                exchange.mutate()
                    .request(exchange.getRequest().mutate()
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .header("X-User", email)
                        .header("X-Role", role)
                        .build())
                    .build()
            );

        } catch (Exception e) {
            System.out.println("❌ JWT ERROR: " + e.getMessage());

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}