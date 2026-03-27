package com.finflow.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocConsumer {

    private final DocumentService documentService;
    private final RabbitTemplate rabbitTemplate;

    public DocConsumer(DocumentService documentService,
                       RabbitTemplate rabbitTemplate) {
        this.documentService = documentService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "doc-status-queue")
    public void receive(String userId) {

        System.out.println("📥 Received request for user: " + userId);

        String status = documentService.getStatus(userId);

        System.out.println("📄 Doc Status: " + status);

        // ✅ SEND RESPONSE BACK
        String response = userId + ":" + status;

        rabbitTemplate.convertAndSend("doc-status-response-queue", response);

        System.out.println("📤 Sent response: " + response);
    }
}