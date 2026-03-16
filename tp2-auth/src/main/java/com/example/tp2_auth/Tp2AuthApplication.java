package com.example.tp2_auth;

import com.example.tp2_auth.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tp2AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tp2AuthApplication.class, args);
	}

	@Bean
	CommandLineRunner initTestUser(AuthService authService) {
		return args -> {
			try {
				authService.register("toto@example.com", "Toto@1234567890");
				System.out.println("Compte de test cree : toto@example.com / Toto@1234567890");
			} catch (Exception e) {
				System.out.println("Compte de test deja existant");
			}
		};
	}
}