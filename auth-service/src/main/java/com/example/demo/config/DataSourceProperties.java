package com.example.demo.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Fail-fast validation for database configuration.
 * The application will refuse to start if DB credentials are missing.
 */
@Validated
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    @NotBlank(message = "spring.datasource.url must not be blank. Set the AUTH_DB_URL environment variable.")
    private String url;

    @NotBlank(message = "spring.datasource.username must not be blank. Set the DB_USERNAME environment variable.")
    private String username;

    @NotBlank(message = "spring.datasource.password must not be blank. Set the DB_PASSWORD environment variable.")
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
