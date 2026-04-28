package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.ApiService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class Maincontroller {

    @Autowired private ApiService apiService;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    // ── Públicas ──────────────────────────────────────────────

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/registro")
    public String registroForm() { return "registro"; }

    @PostMapping("/registro")
    public String registroSubmit(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {
        try {
            userService.registrar(username, password);
            return "redirect:/login?registrado";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }

    // ── Usuario logueado ──────────────────────────────────────

    @GetMapping("/")
    public String index(Authentication auth, Model model) {
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";
        model.addAttribute("username", auth.getName());
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "index";
    }

    @GetMapping("/pokemon")
    public String pokemonPage() { return "pokemon"; }

    @GetMapping("/pokemon/buscar")
    public String buscarPokemon(@RequestParam(required = false) String nombre, Model model) {
        if (nombre != null && !nombre.isBlank()) {
            Map<String, Object> resultado = apiService.getPokemonDetalle(nombre);
            model.addAttribute("resultado", resultado);
        }
        return "pokemon";
    }

    // ── Solo ADMIN ────────────────────────────────────────────

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        List<User> usuarios = userRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "admin/panel";
    }

    @PostMapping("/admin/rol")
    public String cambiarRol(@RequestParam Integer userId,
                             @RequestParam String roleName,
                             Model model) {
        userRepository.findById(userId).ifPresent(user -> {
            var rol = roleRepository.findByRoleName(roleName);
            if (rol != null) {
                user.setUserRole(rol);
                userRepository.save(user);
            }
        });
        return "redirect:/admin";
    }

    @GetMapping("/admin/test")
    public String adminTest() { return "admin/test"; }

    @GetMapping("/admin/test/saludo")
    public String testSaludo(Model model) {
        model.addAttribute("resultado", apiService.getSaludo());
        model.addAttribute("tipo", "Saludo");
        return "admin/test";
    }

    @GetMapping("/admin/test/archivo")
    public String testArchivo(Model model) {
        model.addAttribute("resultado", apiService.testExcepcionArchivo());
        model.addAttribute("tipo", "Excepción de archivo");
        return "admin/test";
    }

    @GetMapping("/admin/test/bbdd")
    public String testBBDD(Model model) {
        model.addAttribute("resultado", apiService.testExcepcionBBDD());
        model.addAttribute("tipo", "Excepción de base de datos");
        return "admin/test";
    }

    @GetMapping("/admin/test/pokemon")
    public String testPokemon(Model model) {
        model.addAttribute("resultado", apiService.testExcepcionPokemon());
        model.addAttribute("tipo", "Excepción de API Pokémon");
        return "admin/test";
    }
}