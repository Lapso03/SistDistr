// service/ReservaService.java
package com.example.demo.service;

import com.example.demo.exception.AforoAgotadoException;
import com.example.demo.exception.ReservaNotFoundException;
import com.example.demo.model.EstadoReserva;
import com.example.demo.model.Evento;
import com.example.demo.model.Reserva;
import com.example.demo.model.User;
import com.example.demo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private UserService userService;

    // ── Lectura ───────────────────────────────────────────────

    public List<Reserva> findByUsuario(User usuario) {
        return reservaRepository.findByUsuarioOrderByFechaReservaDesc(usuario);
    }

    public List<Reserva> findByUsuarioAndEstado(User usuario, EstadoReserva estado) {
        return reservaRepository.findByUsuarioAndEstadoOrderByFechaReservaDesc(usuario, estado);
    }

    public List<Reserva> findByEvento(Integer eventoId) {
        return reservaRepository.findByEventoIdOrderByFechaReservaDesc(eventoId);
    }

    public Reserva findById(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNotFoundException(id));
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    // ── Reservar ──────────────────────────────────────────────

    @Transactional
    public Reserva reservar(Integer eventoId, Integer numEntradas, User usuario, boolean recordatorio) {
        Evento evento = eventoService.findById(eventoId);

        if (!evento.tieneEntradas()) {
            throw new AforoAgotadoException(evento.getNombre());
        }

        if (evento.getAforoDisponible() < numEntradas) {
            throw new AforoAgotadoException(evento.getNombre());
        }

        if (reservaRepository.existsReservaActivaByUsuarioAndEvento(usuario, eventoId)) {
            throw new IllegalStateException("Ya tienes una reserva activa para este evento.");
        }

        Reserva reserva = new Reserva();
        reserva.setEvento(evento);
        reserva.setUsuario(usuario);
        reserva.setNumEntradas(numEntradas);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setRecordatorio(recordatorio);

        reservaRepository.save(reserva); // @PrePersist calcula fecha y precio

        eventoService.incrementarAforo(eventoId, numEntradas);

        return reserva;
    }

    // ── Confirmar (tras pago simulado) ────────────────────────

    @Transactional
    public Reserva confirmar(Integer reservaId) {
        Reserva reserva = findById(reservaId);
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        return reservaRepository.save(reserva);
    }

    // ── Cancelar ──────────────────────────────────────────────

    @Transactional
    public void cancelar(Integer reservaId, User usuario) {
        Reserva reserva = findById(reservaId);

        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalStateException("No puedes cancelar una reserva que no es tuya.");
        }

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("Esta reserva ya está cancelada.");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);

        eventoService.liberarAforo(reserva.getEvento().getId(), reserva.getNumEntradas());
    }
}