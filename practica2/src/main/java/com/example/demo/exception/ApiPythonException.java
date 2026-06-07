package com.example.demo.exception;

public class ApiPythonException extends RuntimeException {
    public ApiPythonException() {
        super("No se puede conectar con la API Python.");
    }
}