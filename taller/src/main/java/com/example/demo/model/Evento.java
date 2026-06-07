package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 150)
    private String lugar;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer aforo;

    @Column(nullable = false)
    private Integer aforoOcupado = 0;

    @Column(length = 500)
    private String imagen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaEvento categoria;

    // ── Calculado ─────────────────────────────────────────────

    public Integer getAforoDisponible() {
        return aforo - aforoOcupado;
    }

    public boolean tieneEntradas() {
        return getAforoDisponible() > 0;
    }
}