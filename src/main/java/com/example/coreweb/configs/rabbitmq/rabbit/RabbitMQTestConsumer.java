package com.example.coreweb.configs.rabbitmq.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author mir00r on 6/2/23
 * @project IntelliJ IDEA
 */
@Component
public class RabbitMQTestConsumer {

    @RabbitListener(id = "${default.rabbitmq.exchange}", queues = {"${default.rabbitmq.queue}"}, concurrency = "2")
    public void receiver(String message) {
        System.out.println("Message Received, message : " + message);
    }
}
