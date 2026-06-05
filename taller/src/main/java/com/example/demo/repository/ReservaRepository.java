package com.example.demo.repository;

import com.example.demo.model.EstadoReserva;
import com.example.demo.model.Reserva;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Reservas de un usuario
    List<Reserva> findByUsuarioOrderByFechaReservaDesc(User usuario);

    // Reservas de un usuario por estado
    List<Reserva> findByUsuarioAndEstadoOrderByFechaReservaDesc(User usuario, EstadoReserva estado);

    // Reservas de un evento (para el admin)
    List<Reserva> findByEventoIdOrderByFechaReservaDesc(Integer eventoId);

    // Eliminar las reservas de un evento para poder eliminarlo
    @Modifying
    @Transactional
    void deleteByEventoId(Integer eventoId);

    // Total de entradas reservadas para un evento (excluye canceladas)
    @Query("SELECT COALESCE(SUM(r.numEntradas), 0) FROM Reserva r WHERE r.evento.id = :eventoId AND r.estado <> 'CANCELADA'")
    Integer sumEntradasByEvento(Integer eventoId);

    // Comprobar si un usuario ya tiene reserva activa en un evento
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.usuario = :usuario AND r.evento.id = :eventoId AND r.estado <> 'CANCELADA'")
    boolean existsReservaActivaByUsuarioAndEvento(User usuario, Integer eventoId);


    //A usar cuando consiga terminar los recordatorios y añadir notificaciones en el front
//    @Query("SELECT r FROM Reserva r WHERE r.recordatorio = true " +
//            "AND r.estado = 'CONFIRMADA' " +
//            "AND r.evento.fecha BETWEEN :ahora AND :hasta")
//    List<Reserva> findReservasConRecordatorioPorFecha(
//            LocalDateTime ahora, LocalDateTime hasta);
}