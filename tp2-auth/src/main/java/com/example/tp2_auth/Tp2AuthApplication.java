package com.example.tp2_auth;

import com.example.tp2_auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tp2AuthApplication {

	private static final Logger logger = LoggerFactory.getLogger(Tp2AuthApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Tp2AuthApplication.class, args);
	}

	@Bean
	CommandLineRunner initTestUser(AuthService authService) {
		return args -> {
			try {
				authService.register("toto@example.com", "Toto@1234567890");
				logger.info("Compte de test cree : toto@example.com");
			} catch (Exception e) {
				logger.info("Compte de test deja existant");
			}
		};
	}
}