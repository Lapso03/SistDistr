package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.EventoService;
import com.example.demo.service.ReservaService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private EventoService eventoService;

    // ── Públicas ──────────────────────────────────────────────

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/registro")
    public String registroForm() { return "registro"; }

    @PostMapping("/registro")
    public String registroSubmit(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam(required = false) String email) {
        userService.registrar(username, password, email);
        return "redirect:/login?registrado";
    }

    @GetMapping("/politica-privacidad")
    public String politicaPrivacidad() {
        return "politicaPrivacidad";
    }

    // ── Usuario logueado ──────────────────────────────────────

    @GetMapping("/")
    public String index(Authentication auth, Model model) {
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";
        model.addAttribute("username", auth.getName());
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOrganizador = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ORGANIZADOR"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("tieneAccesoAdmin", isAdmin || isOrganizador);
        // Muestra todos los próximos, agotados o no
        model.addAttribute("eventos", eventoService.findProximos());
        return "index";
    }

    // ── Solo ADMIN ────────────────────────────────────────────

    @Autowired private ReservaService reservaService;

    @GetMapping("/admin")
    public String adminPanel(Authentication auth, Model model) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Organizador va directo a gestión de eventos
        if (!isAdmin) {
            return "redirect:/admin/eventos";
        }

        List<User> usuarios = userRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("totalEventos", eventoService.findAll().size());
        model.addAttribute("totalReservas", reservaService.findAll().size());
        return "admin/panel";
    }

    @PostMapping("/admin/rol")
    public String cambiarRol(@RequestParam Integer userId,
                             @RequestParam String roleName) {
        userRepository.findById(userId).ifPresent(user -> {
            var rol = roleRepository.findByRoleName(roleName);
            if (rol != null) {
                user.setUserRole(rol);
                userRepository.save(user);
            }
        });
        return "redirect:/admin";
    }
}