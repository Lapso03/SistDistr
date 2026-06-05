package com.example.demo.repository;

import com.example.demo.model.CategoriaEvento;
import com.example.demo.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    // Eventos futuros (página principal)
    List<Evento> findByFechaAfterOrderByFechaAsc(LocalDateTime fecha);

    // Filtrar por categoría
    List<Evento> findByCategoriaOrderByFechaAsc(CategoriaEvento categoria);

    // Búsqueda por nombre (buscador)
    List<Evento> findByNombreContainingIgnoreCaseOrderByFechaAsc(String nombre);

    // Eventos con entradas disponibles
    @Query("SELECT e FROM Evento e WHERE (e.aforo - e.aforoOcupado) > 0 AND e.fecha > :ahora ORDER BY e.fecha ASC")
    List<Evento> findDisponibles(LocalDateTime ahora);

    // Eventos futuros por categoría con entradas
    @Query("SELECT e FROM Evento e WHERE e.categoria = :categoria AND (e.aforo - e.aforoOcupado) > 0 AND e.fecha > :ahora ORDER BY e.fecha ASC")
    List<Evento> findDisponiblesByCategoria(CategoriaEvento categoria, LocalDateTime ahora);
}