package com.finflow.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.finflow.demo.entity.ApplicationStatus;
import com.finflow.demo.entity.LoanApplication;
import com.finflow.demo.repository.ApplicationRepository;

@Component
public class DocStatusConsumer {

    private final ApplicationRepository repository;

    public DocStatusConsumer(ApplicationRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "doc-status-response-queue")
    public void receive(String message) {

        System.out.println("📥 Received response: " + message);

        // Format: userId:VERIFIED
        String[] parts = message.split(":");

        String userId = parts[0];
        String status = parts[1];

        // 🔥 Find latest application
        LoanApplication app = repository.findTopByUserIdOrderByIdDesc(userId)
                .orElse(null);

        if (app == null) {
            System.out.println("❌ No application found for user");
            return;
        }

        // 🚫 Don't override final states
        if (app.getStatus() == ApplicationStatus.APPROVED ||
            app.getStatus() == ApplicationStatus.REJECTED) {
            System.out.println("⛔ Skipping update (final state)");
            return;
        }

        if ("VERIFIED".equalsIgnoreCase(status)) {
            app.setStatus(ApplicationStatus.DOCS_VERIFIED);
        } else {
            app.setStatus(ApplicationStatus.DOCS_PENDING);
        }

        repository.save(app);

        System.out.println("✅ Application updated: " + app.getStatus());
    }
}