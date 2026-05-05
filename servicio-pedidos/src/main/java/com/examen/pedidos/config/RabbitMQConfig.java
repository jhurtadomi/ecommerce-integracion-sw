package com.examen.pedidos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "pedidos.exchange";
    public static final String QUEUE_NAME    = "cola.pedidos.creados";
    public static final String ROUTING_KEY   = "pedido.creado";

    @Bean
    public DirectExchange pedidosExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue pedidosQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding binding(Queue pedidosQueue, DirectExchange pedidosExchange) {
        return BindingBuilder
                .bind(pedidosQueue)
                .to(pedidosExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}