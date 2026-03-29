package com.finflow.demo.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Fail-fast validation for critical JWT configuration.
 * The application will refuse to start if jwt.secret is missing or too short.
 */
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "jwt.secret must not be blank. Set the JWT_SECRET environment variable.")
    @Size(min = 32, message = "jwt.secret must be at least 32 characters for HMAC-SHA256.")
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
