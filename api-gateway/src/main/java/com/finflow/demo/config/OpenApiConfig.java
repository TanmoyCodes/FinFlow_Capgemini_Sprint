package com.finflow.demo.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @org.springframework.beans.factory.annotation.Value("${gateway.secret}")
    private String gatewaySecret;

    @Bean
    public RouteLocator swaggerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-api-docs", r -> r.path("/auth/v3/api-docs")
                        .filters(f -> f.addRequestHeader("X-Gateway-Secret", gatewaySecret))
                        .uri("lb://AUTH-SERVICE"))
                .route("application-api-docs", r -> r.path("/application/v3/api-docs")
                        .filters(f -> f.addRequestHeader("X-Gateway-Secret", gatewaySecret))
                        .uri("lb://APPLICATION-SERVICE"))
                .route("doc-verification-api-docs", r -> r.path("/documents/v3/api-docs")
                        .filters(f -> f.addRequestHeader("X-Gateway-Secret", gatewaySecret))
                        .uri("lb://DOC-VERIFICATION"))
                .build();
    }

    // Programmatic configuration for Swagger UI
    @Bean
    public Set<SwaggerUrl> swaggerUrls(SwaggerUiConfigProperties swaggerUiConfigProperties) {
        Set<SwaggerUrl> urls = new LinkedHashSet<>();
        urls.add(new SwaggerUrl("Auth Service", "/auth/v3/api-docs", "Auth Service"));
        urls.add(new SwaggerUrl("Application Service", "/application/v3/api-docs", "Application Service"));
        urls.add(new SwaggerUrl("Document Verification Service", "/documents/v3/api-docs", "Document Verification Service"));
        swaggerUiConfigProperties.setUrls(urls);
        return urls;
    }
}
