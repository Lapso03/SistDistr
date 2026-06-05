package com.example.demo.exception;

public class ReservaNotFoundException extends RuntimeException {
  public ReservaNotFoundException(String message) {
    super(message);
  }
}
