package com.finflow.demo.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocProducer {

    private final RabbitTemplate rabbitTemplate;

    public DocProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDocRequest(String userId) {
        System.out.println("📤 Sending to queue: " + userId);
        rabbitTemplate.convertAndSend("doc-status-queue", userId);
    }
}