package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository,
								RoleRepository roleRepository,
								PasswordEncoder passwordEncoder) {
		return args -> {
			// Crear rol ADMIN si no existe
			Role adminRole = roleRepository.findByRoleName("ADMIN");
			if (adminRole == null) {
				adminRole = new Role();
				adminRole.setRoleName("ADMIN");
				adminRole.setShowOnCreate(0);
				roleRepository.save(adminRole);
			}

			// Crear rol USER si no existe
			Role userRole = roleRepository.findByRoleName("USER");
			if (userRole == null) {
				userRole = new Role();
				userRole.setRoleName("USER");
				userRole.setShowOnCreate(1);
				roleRepository.save(userRole);
			}

			// Crear usuario admin si no existe
			if (userRepository.findUserByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setUserRole(adminRole);
				userRepository.save(admin);
				System.out.println(">>> Usuario admin creado con contraseña: admin123");
			}
		};
	}
}