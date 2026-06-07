package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false)
    private Integer numEntradas;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    @Column(nullable = false)
    private boolean recordatorio = false;

    // ── Calculado antes de persistir ──────────────────────────

    @PrePersist
    public void prePersist() {
        this.fechaReserva = LocalDateTime.now();
        this.precioTotal = evento.getPrecio()
                .multiply(BigDecimal.valueOf(numEntradas));
    }
}