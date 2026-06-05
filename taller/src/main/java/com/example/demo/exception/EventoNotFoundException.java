package com.example.demo.exception;

public class EventoNotFoundException extends RuntimeException {
  public EventoNotFoundException(String message) {
    super(message);
  }
}
