package com.example.demo.exception;

public class ApiTimeoutException extends RuntimeException {
    public ApiTimeoutException() {
        super("La API ha tardado demasiado en responder.");
    }
}