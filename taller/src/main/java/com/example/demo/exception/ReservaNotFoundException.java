// exception/ReservaNotFoundException.java
package com.example.demo.exception;

public class ReservaNotFoundException extends RuntimeException {
    public ReservaNotFoundException(Integer id) {
        super("La reserva con id '" + id + "' no existe.");
    }
}