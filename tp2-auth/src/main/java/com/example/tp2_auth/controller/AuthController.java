package com.example.tp2_auth.controller;

import com.example.tp2_auth.entity.User;
import com.example.tp2_auth.exception.AuthenticationFailedException;
import com.example.tp2_auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST gérant les endpoints d'authentification.
 * TP2 améliore le stockage mais ne protège pas encore contre le rejeu.
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Inscrit un nouvel utilisateur.
     * POST /api/auth/register
     */
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestParam String email,
                                           @RequestParam String password) {
        authService.register(email, password);
        return ResponseEntity.ok("Inscription reussie");
    }

    /**
     * Connecte un utilisateur et retourne un token.
     * POST /api/auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password) {
        String token = authService.login(email, password);
        return ResponseEntity.ok("Login reussi. Token: " + token);
    }

    /**
     * Retourne les infos de l'utilisateur connecté.
     * GET /api/me — nécessite un token valide dans le header Authorization.
     */
    @GetMapping("/me")
    public ResponseEntity<String> me(@RequestHeader(value = "Authorization",
            required = false) String token) {
        if (token == null || token.isBlank()) {
            throw new AuthenticationFailedException("Token manquant");
        }
        User user = authService.getUserByToken(token);
        return ResponseEntity.ok("Utilisateur connecte : " + user.getEmail());
    }
}