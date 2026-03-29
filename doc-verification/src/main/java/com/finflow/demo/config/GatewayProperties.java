package com.finflow.demo.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Fail-fast validation for gateway secret configuration.
 */
@Validated
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

    @NotBlank(message = "gateway.secret must not be blank. Set the GATEWAY_SECRET environment variable.")
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
