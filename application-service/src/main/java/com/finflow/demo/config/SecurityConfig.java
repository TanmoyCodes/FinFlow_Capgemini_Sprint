package com.finflow.demo.config;

import com.finflow.demo.security.JwtFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

            	    // ✅ Swagger FIRST
            	    .requestMatchers(
            	        "/error",
            	        "/swagger-ui/**",
            	        "/v3/api-docs/**",
            	        "/application/v3/api-docs/**",
            	        "/swagger-resources/**",
            	        "/webjars/**"
            	    ).permitAll()

            	    // ADMIN ONLY
            	    .requestMatchers("/application/admin/**").hasRole("ADMIN")

            	    // USER + ADMIN
            	    .requestMatchers("/application/**").hasAnyRole("USER", "ADMIN")

            	    .anyRequest().authenticated()
            	)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}