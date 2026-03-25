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

        // 🔥 REQUIRED (fixes your 403)
        headers.set("X-Gateway-Secret", "my-secret-key");

        // ✅ Forward JWT (good practice)
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
}