package com.example.demo.service;

import com.example.demo.exception.EventoNotFoundException;
import com.example.demo.exception.AforoAgotadoException;
import com.example.demo.model.CategoriaEvento;
import com.example.demo.model.Evento;
import com.example.demo.repository.EventoRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    // ── Lectura ───────────────────────────────────────────────

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public List<Evento> findProximos() {
        return eventoRepository.findByFechaAfterOrderByFechaAsc(LocalDateTime.now());
    }

    public List<Evento> findDisponibles() {
        return eventoRepository.findDisponibles(LocalDateTime.now());
    }

    public List<Evento> findByCategoria(CategoriaEvento categoria) {
        return eventoRepository.findByCategoriaOrderByFechaAsc(categoria);
    }

    public List<Evento> findDisponiblesByCategoria(CategoriaEvento categoria) {
        return eventoRepository.findDisponiblesByCategoria(categoria, LocalDateTime.now());
    }

    public List<Evento> findByNombre(String nombre) {
        return eventoRepository.findByNombreContainingIgnoreCaseOrderByFechaAsc(nombre);
    }

    public Evento findById(Integer id) {
        return eventoRepository.findById(id)
                .orElseThrow(EventoNotFoundException::new);
    }

    // ── Escritura ─────────────────────────────────────────────

    public void guardar(Evento evento) {
        if (evento.getAforoOcupado() == null) {
            evento.setAforoOcupado(0);
        }
        eventoRepository.save(evento);
    }

    @Transactional
    public void eliminar(Integer id) {
        findById(id);
        reservaRepository.deleteByEventoId(id);
        eventoRepository.deleteById(id);
    }

    // ── Aforo ─────────────────────────────────────────────────

    public void incrementarAforo(Integer id, Integer entradas) {
        Evento evento = findById(id);
        if (evento.getAforoDisponible() < entradas) {
            throw new AforoAgotadoException(evento.getNombre());
        }
        evento.setAforoOcupado(evento.getAforoOcupado() + entradas);
        eventoRepository.save(evento);
    }

    public void liberarAforo(Integer id, Integer entradas) {
        Evento evento = findById(id);
        evento.setAforoOcupado(Math.max(0, evento.getAforoOcupado() - entradas));
        eventoRepository.save(evento);
    }
}