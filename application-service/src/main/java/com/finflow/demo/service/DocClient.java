package com.finflow.demo.service;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DocClient {

    private final RestTemplate restTemplate;

    public DocClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getDocStatus(String userId, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Gateway-Secret", "my-secret-key");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://DOC-VERIFICATION/documents/status/" + userId,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    public long getUserCount(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Gateway-Secret", "my-secret-key");
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Long> response = restTemplate.exchange(
                    "http://AUTH-SERVICE/auth/internal/user-count",
                    HttpMethod.GET,
                    entity,
                    Long.class
            );

            return response.getBody() != null ? response.getBody() : 0;
        } catch (Exception e) {
            System.out.println("⚠️ Could not fetch user count from auth-service: " + e.getMessage());
            return 0;
        }
    }
}
