package com.finflow.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    public static final String REQUEST_QUEUE = "doc-status-queue";
    public static final String RESPONSE_QUEUE = "doc-status-response-queue";

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE, true); // durable
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE, true); // durable
    }
}