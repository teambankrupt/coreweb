package com.example.coreweb.configs.rabbitmq.services;

import com.example.common.utils.ExceptionUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author mir00r on 3/2/23
 * @project IntelliJ IDEA
 */
@Service
public class RabbitMQService {

    @Value("${default.rabbitmq.routing.key}")
    private String defaultRoutingKey;

    @Value("${default.rabbitmq.exchange}")
    private String defaultExchange;

    @Value("${default.rabbitmq.queue}")
    private String defaultQueue;

    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public RabbitMQService(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void createExchange(String exchangeName, String exchangeType, boolean durable, boolean autoDelete) {
        Exchange exchange = ExchangeBuilder.directExchange(exchangeName)
                .durable(durable)
                .autoDelete()
                .build();
        this.rabbitAdmin.declareExchange(exchange);
    }

    public void createQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete) {
        Queue queue = QueueBuilder.durable(queueName)
                .exclusive()
                .autoDelete()
                .build();
        this.rabbitAdmin.declareQueue(queue);
    }

    public void createBinding(String exchangeName, String queueName, String routingKey) {
        Binding binding = BindingBuilder.bind(new Queue(queueName)).to(new DirectExchange(exchangeName)).with(routingKey);
        this.rabbitAdmin.declareBinding(binding);
    }

    public void publishMessage(String message, String exchange, String queue, String routingKey) {
        this.rabbitAdmin.declareExchange(new DirectExchange(exchange));
        this.rabbitAdmin.declareQueue(new Queue(queue));
        this.rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(queue))
                .to(new DirectExchange(exchange))
                .with(routingKey));

        this.rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void publishMessage(Object object, String exchange, String exchangeType, String queue, String routingKey) {
        if (object == null || exchange == null || exchangeType == null || queue == null || routingKey == null) throw ExceptionUtil.Companion.invalid("Please provide required information");
        this.rabbitAdmin.declareExchange(new ExchangeBuilder(exchange, exchangeType).build());
        this.rabbitAdmin.declareQueue(new Queue(queue));
        this.rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(queue))
                .to(new DirectExchange(exchange))
                .with(routingKey));

        this.rabbitTemplate.convertAndSend(exchange, routingKey, object);
    }
}
