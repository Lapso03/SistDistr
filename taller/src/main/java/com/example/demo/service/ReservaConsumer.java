package com.example.demo.service;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.ReservaRequestDTO;
import com.example.demo.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaConsumer {

    @Autowired private ReservaService reservaService;
    @Autowired private UserService userService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVAS)
    public void procesarReserva(ReservaRequestDTO request) {
        try {
            User usuario = userService.buscar(request.getUsername());
            if (usuario == null) return;
            reservaService.reservar(
                    request.getEventoId(),
                    request.getNumEntradas(),
                    usuario,
                    request.isRecordatorio()
            );
        } catch (Exception e) {
            // Si falla (aforo agotado, ya tiene reserva, etc.) se descarta silenciosamente
            // Se enviaría notificación de error al usuario
            System.out.println("Reserva descartada para " + request.getUsername()
                    + ": " + e.getMessage());
        }
    }
}