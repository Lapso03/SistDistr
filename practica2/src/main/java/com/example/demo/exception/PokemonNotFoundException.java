package com.example.demo.exception;

public class PokemonNotFoundException extends RuntimeException {
    public PokemonNotFoundException(String nombre) {
        super("El Pokémon '" + nombre + "' no existe.");
    }
}