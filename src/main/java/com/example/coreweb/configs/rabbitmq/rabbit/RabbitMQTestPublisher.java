package com.example.coreweb.configs.rabbitmq.rabbit;

import com.example.coreweb.configs.rabbitmq.model.ExchangeTypes;
import com.example.coreweb.configs.rabbitmq.services.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author mir00r on 6/2/23
 * @project IntelliJ IDEA
 */
@Component
public class RabbitMQTestPublisher {

    @Value("${default.rabbitmq.routing.key}")
    private String testRoutingKey;

    @Value("${default.rabbitmq.exchange}")
    private String defaultExchange;

    @Value("${default.rabbitmq.queue}")
    private String defaultQueue;

    private final RabbitMQService rabbitMQService;

    @Autowired
    public RabbitMQTestPublisher(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    public void send(String message, String exchange, ExchangeTypes exchangeTypes, String queueName, String routingKey) {
        System.out.println("sending message to queue with name :" + queueName + ", exchange: " + exchange + ", exchange type: " + exchangeTypes.getLabel() + ", routing key: " + routingKey + " and message : " + message);
        this.rabbitMQService.publishMessage(message,
                exchange == null ? this.defaultExchange : exchange,
                exchangeTypes.getLabel(),
                queueName == null ? this.defaultQueue : queueName,
                routingKey == null ? this.testRoutingKey : routingKey);
        System.out.println("message sent to rabbit queue");
    }
}
