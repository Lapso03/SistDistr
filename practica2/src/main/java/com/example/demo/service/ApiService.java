package com.example.demo.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:5000";

    private Map<String, String> llamarFlask(String endpoint) {
        Map<String, String> resultado = new HashMap<>();
        try {
            String respuesta = restTemplate.getForObject(BASE_URL + endpoint, String.class);
            resultado.put("estado", "ok");
            resultado.put("mensaje", respuesta);
        } catch (ResourceAccessException e) {
            // Flask no está corriendo o no hay conexión
            resultado.put("estado", "error");
            resultado.put("mensaje", "No se puede conectar con la API Python. ¿Está arrancada?");
        } catch (HttpClientErrorException e) {
            // Error 4xx desde Flask
            resultado.put("estado", "error");
            resultado.put("mensaje", "Error en la petición: " + e.getStatusCode());
        } catch (HttpServerErrorException e) {
            // Error 5xx desde Flask — excepción no crítica, mostramos info traducida
            resultado.put("estado", "error");
            resultado.put("mensaje", traducirError(e.getResponseBodyAsString()));
        }
        return resultado;
    }

    // Traduce el mensaje de error de Flask a español legible
    private String traducirError(String errorFlask) {
        if (errorFlask.contains("FileNotFoundError")) {
            return "Error: El archivo solicitado no existe en el servidor.";
        } else if (errorFlask.contains("PermissionError")) {
            return "Error: Sin permisos para leer el archivo.";
        } else if (errorFlask.contains("OperationalError")) {
            return "Error: No se pudo conectar a la base de datos.";
        } else if (errorFlask.contains("requests.exceptions")) {
            return "Error: Fallo al llamar a la API de Pokémon.";
        } else {
            return "Error en el servidor Python: " + errorFlask;
        }
    }

    public Map<String, Object> getPokemonDetalle(String nombre) {
        Map<String, Object> resultado = new HashMap<>();
        try {
            // Usamos ParameterizedTypeReference para deserializar el JSON completo
            ResponseEntity<Map<String, Object>> response = new RestTemplate().exchange(
                    BASE_URL + "/api/pokemon/" + nombre,
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body != null && !body.containsKey("error")) {
                body.put("estado", "ok");
                resultado = body;
            }
        } catch (HttpServerErrorException e) {
            resultado.put("estado", "error");
            resultado.put("mensaje", traducirError(e.getResponseBodyAsString()));
        } catch (ResourceAccessException e) {
            resultado.put("estado", "error");
            resultado.put("mensaje", "No se puede conectar con la API Python. ¿Está arrancada?");
        }
        return resultado;
    }

    public Map<String, String> getSaludo() {
        return llamarFlask("/api/saludo");
    }

    public Map<String, String> testExcepcionArchivo() {
        return llamarFlask("/api/exception/archivo");
    }

    public Map<String, String> testExcepcionBBDD() {
        return llamarFlask("/api/exception/bbdd");
    }

    public Map<String, String> testExcepcionPokemon() {
        return llamarFlask("/api/exception/pokemon");
    }
}