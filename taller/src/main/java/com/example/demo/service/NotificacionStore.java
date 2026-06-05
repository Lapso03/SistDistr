package com.example.demo.service;

import com.example.demo.dto.EmailDTO;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificacionStore {

    private final Map<String, List<EmailDTO>> store = new ConcurrentHashMap<>();

    public void guardar(String username, EmailDTO notif) {
        store.computeIfAbsent(username, k -> new ArrayList<>()).add(0, notif);
        // Máximo 10 notificaciones por usuario
        List<EmailDTO> lista = store.get(username);
        if (lista.size() > 10) lista.remove(lista.size() - 1);
    }

    public List<EmailDTO> obtener(String username) {
        return store.getOrDefault(username, new ArrayList<>());
    }

    public void marcarLeidas(String username) {
        store.remove(username);
    }

    public int contarNoLeidas(String username) {
        return store.getOrDefault(username, new ArrayList<>()).size();
    }
}