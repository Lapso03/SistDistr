package com.example.demo.service;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.ReservaRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaProducer {

    @Autowired private RabbitTemplate rabbitTemplate;

    public void enviarSolicitudReserva(ReservaRequestDTO request) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_RESERVA,
                request
        );
    }
}