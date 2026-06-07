package com.example.demo.controller;

import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.UsernameAlreadyExistsException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("usuarios", userRepository.findAll());
        return "admin/usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("usuario", new User());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("accion", "Crear");
        return "admin/usuarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("accion", "Editar");
        return "admin/usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Integer id,
                          @RequestParam String username,
                          @RequestParam(required = false) String password,
                          @RequestParam(required = false) String email,
                          @RequestParam Integer roleId,
                          Model model) {
        Role rol = roleRepository.findById(roleId).orElse(null);
        if (rol == null) return "redirect:/admin/usuarios";

        if (id == null) {
            // Crear nuevo
            if (userRepository.findUserByUsername(username).isPresent()) {
                throw new UsernameAlreadyExistsException(username);
            }

            if (email != null && !email.isBlank() &&
                    userRepository.findByEmail(email).isPresent()) {

                throw new EmailAlreadyExistsException(email);
            }

            User nuevo = new User();
            nuevo.setUsername(username);
            nuevo.setPassword(passwordEncoder.encode(password));
            nuevo.setEmail(email);
            nuevo.setUserRole(rol);
            userRepository.save(nuevo);
        } else {
            // Editar existente
            userRepository.findById(id).ifPresent(user -> {

                userRepository.findByEmail(email)
                                .ifPresent(existing -> {
                                    if (!existing.getId().equals(user.getId())) {
                                        throw new EmailAlreadyExistsException(email);
                                    }
                                        });

                user.setUsername(username);
                user.setEmail(email);
                if (password != null && !password.isBlank()) {
                    user.setPassword(passwordEncoder.encode(password));
                }
                user.setUserRole(rol);
                userRepository.save(user);
            });
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "redirect:/admin/usuarios";
    }
}