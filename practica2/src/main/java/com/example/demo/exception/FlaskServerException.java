package com.example.demo.exception;

public class FlaskServerException extends RuntimeException {
    public FlaskServerException(String mensaje) {
        super(mensaje);
    }
}