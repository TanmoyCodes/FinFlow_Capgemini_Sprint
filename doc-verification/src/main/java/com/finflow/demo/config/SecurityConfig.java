package com.finflow.demo.config;

import com.finflow.demo.security.GatewayValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final GatewayValidationFilter gatewayFilter;

    public SecurityConfig(GatewayValidationFilter gatewayFilter) {
        this.gatewayFilter = gatewayFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers(
            	        "/error",
            	        "/swagger-ui/**",
            	        "/v3/api-docs/**",
            	        "/documents/v3/api-docs/**",
            	        "/swagger-resources/**",
            	        "/webjars/**"
            	    ).permitAll()

            	    // ALSO allow gateway-based swagger path
            	    .requestMatchers("/application/v3/api-docs").permitAll()

            	    .requestMatchers("/documents/**").permitAll()
            	    .anyRequest().authenticated()
            	)
            .addFilterBefore(gatewayFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
