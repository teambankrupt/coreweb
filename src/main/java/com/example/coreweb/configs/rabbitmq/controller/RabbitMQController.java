package com.example.coreweb.configs.rabbitmq.controller;

import com.example.coreweb.configs.rabbitmq.model.ExchangeTypes;
import com.example.coreweb.configs.rabbitmq.rabbit.RabbitMQTestPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mir00r on 6/2/23
 * @project IntelliJ IDEA
 */
@RestController
@RequestMapping("/api/v1/rabbit-queue")
public class RabbitMQController {

    private final RabbitMQTestPublisher rabbitMQPublisher;

    @Autowired
    public RabbitMQController(RabbitMQTestPublisher rabbitMQPublisher) {
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    @GetMapping("/send-message/{message}")
    public void sendMessage(@PathVariable String message,
                            @RequestParam String queueName, @RequestParam String exchangeName,
                            @RequestParam ExchangeTypes exchangeType, @RequestParam String routingKey) {
        this.rabbitMQPublisher.send(message, exchangeName, exchangeType, queueName, routingKey);
    }
}
