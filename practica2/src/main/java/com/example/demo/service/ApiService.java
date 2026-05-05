package com.example.demo.service;

import com.example.demo.exception.ApiPythonException;
import com.example.demo.exception.ApiTimeoutException;
import com.example.demo.exception.FlaskServerException;
import com.example.demo.exception.PokemonNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:5000";

    // ── Método base: lanza excepciones, no devuelve mapas de error ──

    private String llamarFlask(String endpoint) {
        try {
            return restTemplate.getForObject(BASE_URL + endpoint, String.class);
        } catch (ResourceAccessException e) {
            throw new ApiPythonException();
        } catch (HttpClientErrorException e) {
            throw new FlaskServerException("Error en la petición: " + e.getStatusCode());
        } catch (HttpServerErrorException e) {
            throw new FlaskServerException(traducirError(e.getResponseBodyAsString()));
        }
    }

    private String traducirError(String errorFlask) {
        if (errorFlask.contains("FileNotFoundError"))      return "El archivo solicitado no existe en el servidor.";
        if (errorFlask.contains("PermissionError"))        return "Sin permisos para leer el archivo.";
        if (errorFlask.contains("OperationalError"))       return "No se pudo conectar a la base de datos.";
        if (errorFlask.contains("requests.exceptions"))    return "Fallo al llamar a la API de Pokémon.";
        return "Error en el servidor Python: " + errorFlask;
    }

    public Map<String, Object> getPokemonDetalle(String nombre) {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    BASE_URL + "/api/pokemon/" + nombre,
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<>() {}
            );

            Map<String, Object> body = response.getBody();

            if (body != null) {
                body.put("estado", "ok");
            }

            return body;

        } catch (HttpClientErrorException.NotFound e) {
            throw new PokemonNotFoundException(nombre);
        } catch (ResourceAccessException e) {
            if (e.getMessage().contains("timed out")) {
                throw new ApiTimeoutException();
            }
            throw new ApiPythonException();
        } catch (HttpServerErrorException e) {
            throw new FlaskServerException(traducirError(e.getResponseBodyAsString()));
        }
    }

    // ── Endpoints de test ────────────────────────────────────────────

    public Map<String, String> getSaludo() {
        Map<String, String> resultado = new HashMap<>();
        resultado.put("estado", "ok");
        resultado.put("mensaje", llamarFlask("/api/saludo"));
        return resultado;
    }

    public Map<String, String> testExcepcionArchivo() {
        Map<String, String> resultado = new HashMap<>();
        resultado.put("estado", "ok");
        resultado.put("mensaje", llamarFlask("/api/exception/archivo"));
        return resultado;
    }

    public Map<String, String> testExcepcionBBDD() {
        Map<String, String> resultado = new HashMap<>();
        resultado.put("estado", "ok");
        resultado.put("mensaje", llamarFlask("/api/exception/bbdd"));
        return resultado;
    }

    public Map<String, String> testExcepcionPokemon() {
        Map<String, String> resultado = new HashMap<>();
        resultado.put("estado", "ok");
        resultado.put("mensaje", llamarFlask("/api/exception/pokemon"));
        return resultado;
    }
}