package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Cola de notificaciones
    public static final String QUEUE_NOTIFICACIONES = "notificaciones.eventos";
    public static final String EXCHANGE = "eventapp.exchange";
    public static final String ROUTING_KEY = "notificacion.evento";

    // Cola de reservas
    public static final String QUEUE_RESERVAS = "reservas.solicitudes";
    public static final String ROUTING_KEY_RESERVA = "reserva.solicitud";

    @Bean public Queue queueNotificaciones() { return new Queue(QUEUE_NOTIFICACIONES, true); }
    @Bean public Queue queueReservas() { return new Queue(QUEUE_RESERVAS, true); }
    @Bean public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }

    @Bean
    public Binding bindingNotificaciones(Queue queueNotificaciones, TopicExchange exchange) {
        return BindingBuilder.bind(queueNotificaciones).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingReservas(Queue queueReservas, TopicExchange exchange) {
        return BindingBuilder.bind(queueReservas).to(exchange).with(ROUTING_KEY_RESERVA);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(messageConverter());
        return t;
    }
}