package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 🔹 Generate Token (ONLY used in Auth Service)
    public String generateToken(String username, List<String> roles) {

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Extract Username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // 🔹 Extract Roles
    public List<String> extractRoles(String token) {

        Object rolesObj = extractClaims(token).get("roles");

        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj)
                    .stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of(); // fallback
    }

    // 🔹 Validate Token
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 🔹 Extract All Claims
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}