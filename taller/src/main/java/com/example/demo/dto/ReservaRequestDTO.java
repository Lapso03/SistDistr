package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequestDTO {
    private Integer eventoId;
    private Integer numEntradas;
    private String username;
    private boolean recordatorio;
}