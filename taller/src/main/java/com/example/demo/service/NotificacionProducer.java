package com.example.demo.service;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.NotificacionDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionProducer {

    @Autowired private RabbitTemplate rabbitTemplate;

    public void enviar(NotificacionDTO notif) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                notif
        );
    }
}