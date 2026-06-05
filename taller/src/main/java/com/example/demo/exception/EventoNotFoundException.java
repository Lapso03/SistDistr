package com.example.demo.exception;

public class EventoNotFoundException extends RuntimeException {
    public EventoNotFoundException() {
        super("Este evento no existe.");
    }
}