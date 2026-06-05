package com.example.demo.controller;

import com.example.demo.model.CategoriaEvento;
import com.example.demo.model.Evento;
import com.example.demo.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // ── Públicas ──────────────────────────────────────────────

    @GetMapping("/eventos/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("evento", eventoService.findById(id));
        return "eventos/detalle";
    }

    @GetMapping("/eventos/buscar")
    public String buscar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) CategoriaEvento categoria,
                         Model model) {
        if (nombre != null && !nombre.isBlank()) {
            model.addAttribute("eventos", eventoService.findByNombre(nombre));
            model.addAttribute("busqueda", nombre);
        } else if (categoria != null) {
            model.addAttribute("eventos", eventoService.findDisponiblesByCategoria(categoria));
            model.addAttribute("categoriaSeleccionada", categoria);
        } else {
            model.addAttribute("eventos", eventoService.findDisponibles());
        }
        model.addAttribute("categorias", CategoriaEvento.values());
        return "eventos/buscar";
    }

    // ── Solo ADMIN ────────────────────────────────────────────

    @GetMapping("/admin/eventos")
    public String lista(Model model) {
        model.addAttribute("eventos", eventoService.findAll());
        return "admin/eventos/lista";
    }

    @GetMapping("/admin/eventos/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("evento", new Evento());
        model.addAttribute("categorias", CategoriaEvento.values());
        model.addAttribute("accion", "Crear");
        return "admin/eventos/formulario";
    }

    @GetMapping("/admin/eventos/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model) {
        model.addAttribute("evento", eventoService.findById(id));
        model.addAttribute("categorias", CategoriaEvento.values());
        model.addAttribute("accion", "Editar");
        return "admin/eventos/formulario";
    }

    @PostMapping("/admin/eventos/guardar")
    public String guardar(@ModelAttribute Evento evento) {
        eventoService.guardar(evento);
        return "redirect:/admin/eventos";
    }

    @PostMapping("/admin/eventos/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        eventoService.eliminar(id);
        return "redirect:/admin/eventos";
    }
}