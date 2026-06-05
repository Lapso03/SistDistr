package com.example.demo.service;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.EmailDTO;
import com.example.demo.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionConsumer {

    @Autowired private NotificacionStore notificacionStore;
    @Autowired private EmailService emailService;
    @Autowired private UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICACIONES)
    public void recibir(EmailDTO notif) {

        notificacionStore.guardar(notif.getUsername(), notif);

        userRepository.findUserByUsername(notif.getUsername())
                .ifPresent(user -> {
                    if (user.getEmail() != null && !user.getEmail().isBlank()) {
                        emailService.enviarRecordatorio(
                                user.getEmail(),
                                notif
                        );
                    }
                });
    }
}