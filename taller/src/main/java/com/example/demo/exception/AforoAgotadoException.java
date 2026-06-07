package com.example.demo.exception;

public class AforoAgotadoException extends RuntimeException {
    public AforoAgotadoException(String nombreEvento) {
        super("No quedan entradas disponibles para '" + nombreEvento + "'.");
    }
}