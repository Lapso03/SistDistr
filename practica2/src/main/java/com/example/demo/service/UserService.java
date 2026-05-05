package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public User buscar(String username) {
        return repository.findUserByUsername(username).orElse(null);
    }

    public void registrar(String username, String password) throws Exception {
        if (repository.findUserByUsername(username).isPresent()) {
            throw new Exception("El nombre de usuario '" + username + "' ya está en uso.");
        }

        Role rol = roleRepository.findByRoleName("USER");
        if (rol == null) {
            rol = new Role();
            rol.setRoleName("USER");
            rol.setShowOnCreate(1);
            roleRepository.save(rol);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRole(rol);

        repository.save(user);
    }
}