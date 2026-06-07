// controller/ReservaController.java
package com.example.demo.controller;

import com.example.demo.dto.ReservaRequestDTO;
import com.example.demo.model.Reserva;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@Controller
public class ReservaController {

    @Autowired private ReservaService reservaService;
    @Autowired private EventoService eventoService;
    @Autowired private UserService userService;
    @Autowired private ReservaProducer reservaProducer;
    @Autowired private EmailService emailService;
    @Autowired private UserRepository userRepository;

    // ── Flujo de reserva ──────────────────────────────────────

    @GetMapping("/reservar/{eventoId}")
    public String formularioReserva(@PathVariable Integer eventoId, Model model) {
        model.addAttribute("evento", eventoService.findById(eventoId));
        return "reserva/formulario";
    }

    @PostMapping("/reservar/{eventoId}")
    public String procesarReserva(@PathVariable Integer eventoId,
                                  @RequestParam Integer numEntradas,
                                  @RequestParam(required = false, defaultValue = "false")
                                  boolean recordatorio,
                                  Authentication auth,
                                  Model model) {
        User usuario = userService.buscar(auth.getName());

        // Crear reserva directamente — flujo de pago se mantiene
        Reserva reserva = reservaService.reservar(eventoId, numEntradas,
                usuario, recordatorio);

        // También notificar a la cola para procesamiento asíncrono
        ReservaRequestDTO request = new ReservaRequestDTO(
                eventoId, numEntradas, auth.getName(), recordatorio
        );
        reservaProducer.enviarSolicitudReserva(request);

        model.addAttribute("reserva", reserva);
        model.addAttribute("evento", reserva.getEvento());
        return "reserva/pago";
    }

    @PostMapping("/reservar/confirmar/{reservaId}")
    public String confirmarPago(@PathVariable Integer reservaId) {
        Reserva reserva = reservaService.confirmar(reservaId);

        // Enviar email de confirmación si tiene email
        userRepository.findUserByUsername(
                reserva.getUsuario().getUsername()
        ).ifPresent(user -> {
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm");
                emailService.enviarConfirmacionReserva(
                        user.getEmail(),
                        reserva.getEvento().getNombre(),
                        reserva.getEvento().getFecha().format(fmt),
                        reserva.getEvento().getLugar(),
                        "EVT-" + reserva.getId(),
                        reserva.getNumEntradas(),
                        reserva.getPrecioTotal().toString()
                );
            }
        });

        return "redirect:/reservar/confirmacion/" + reservaId;
    }

    @GetMapping("/reservar/confirmacion/{reservaId}")
    public String confirmacion(@PathVariable Integer reservaId, Model model) {
        model.addAttribute("reserva", reservaService.findById(reservaId));
        return "reserva/confirmacion";
    }

    // ── Mis reservas ──────────────────────────────────────────

    @GetMapping("/usuario/reservas")
    public String misReservas(Authentication auth, Model model) {
        User usuario = userService.buscar(auth.getName());
        model.addAttribute("reservas", reservaService.findByUsuario(usuario));
        return "usuario/reservas/lista";
    }

    @GetMapping("/usuario/reservas/{id}")
    public String detalleReserva(@PathVariable Integer id,
                                 Authentication auth,
                                 Model model) {
        User usuario = userService.buscar(auth.getName());
        Reserva reserva = reservaService.findById(id);

        // Verificar que la reserva pertenece al usuario
        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/usuario/reservas";
        }

        model.addAttribute("reserva", reserva);
        return "usuario/reservas/detalle";
    }

    @PostMapping("/usuario/reservas/cancelar/{id}")
    public String cancelar(@PathVariable Integer id, Authentication auth) {
        User usuario = userService.buscar(auth.getName());
        reservaService.cancelar(id, usuario);
        return "redirect:/usuario/reservas";
    }
}