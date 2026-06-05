package com.example.demo.service;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.EmailDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionProducer {

    @Autowired private RabbitTemplate rabbitTemplate;

    public void enviar(EmailDTO notif) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                notif
        );
    }
}