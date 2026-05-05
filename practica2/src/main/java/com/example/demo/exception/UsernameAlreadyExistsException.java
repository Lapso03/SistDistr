package com.example.demo.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("El usuario '" + username + "' ya existe.");
    }
}