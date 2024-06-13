package com.example.coreweb.configs.rabbitmq.config;

import com.example.coreweb.configs.rabbitmq.model.RabbitMQConfigModel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * @author mir00r on 3/2/23
 * @project IntelliJ IDEA
 */
@Configuration
public class RabbitMQConfig implements RabbitListenerConfigurer {
    private final ConnectionFactory connectionFactory;
    private final RabbitMQConfigModel rabbitMqConfigModel;

    @Autowired
    public RabbitMQConfig(ConnectionFactory connectionFactory, RabbitMQConfigModel rabbitMqConfigModel) {
        this.connectionFactory = connectionFactory;
        this.rabbitMqConfigModel = rabbitMqConfigModel;
    }

//    @Bean
//    public Queue defaultQueue() {
//        return new Queue(this.rabbitMqConfigModel.getQueue(), true);
//    }
//
//    @Bean
//    public TopicExchange defaultExchange() {
//        return new TopicExchange(this.rabbitMqConfigModel.getExchange());
//    }
//
//    @Bean
//    public Binding defaultBinding() {
//        return BindingBuilder.bind(defaultQueue()).to(defaultExchange()).with(this.rabbitMqConfigModel.getRoutingKey());
//    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMqConfigModel.getHost());
        connectionFactory.setUsername(rabbitMqConfigModel.getUsername());
        connectionFactory.setPassword(rabbitMqConfigModel.getPassword());
        connectionFactory.setPort(rabbitMqConfigModel.getPort());
        return connectionFactory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(1);
        factory.setConsecutiveActiveTrigger(1);
        factory.setConsecutiveIdleTrigger(1);
        factory.setConnectionFactory(connectionFactory());
        registrar.setContainerFactory(factory);
        registrar.setEndpointRegistry(rabbitListenerEndpointRegistry());
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
