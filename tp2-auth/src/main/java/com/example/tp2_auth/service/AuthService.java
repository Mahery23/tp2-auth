package com.example.tp2_auth.service;

import com.example.tp2_auth.entity.User;
import com.example.tp2_auth.exception.*;
import com.example.tp2_auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service principal gérant l'inscription et la connexion des utilisateurs.
 * TP2 améliore le stockage, mais ne protège pas encore contre le rejeu.
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 2;

    private final UserRepository userRepository;
    private final PasswordPolicyValidator passwordPolicyValidator;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository,
                       PasswordPolicyValidator passwordPolicyValidator) {
        this.userRepository = userRepository;
        this.passwordPolicyValidator = passwordPolicyValidator;
    }

    /**
     * Inscrit un nouvel utilisateur avec mot de passe hashé en BCrypt.
     */
    public User register(String email, String password) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            logger.warn("Inscription echouee : email invalide");
            throw new InvalidInputException("Email invalide");
        }
        passwordPolicyValidator.validate(password);

        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("Inscription echouee : email deja existant - {}", email);
            throw new ResourceConflictException("Email deja utilise");
        }

        String hash = passwordEncoder.encode(password);
        User user = new User(email, hash);
        userRepository.save(user);
        logger.info("Inscription reussie pour : {}", email);
        return user;
    }

    /**
     * Connecte un utilisateur avec vérification BCrypt et anti brute force.
     */
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Connexion echouee : email inconnu - {}", email);
                    return new AuthenticationFailedException("Email ou mot de passe incorrect");
                });

        // Vérifier si le compte est bloqué
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            throw new AccountLockedException("Compte bloque. Reessayez dans 2 minutes");
        }

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.getFailedAttempts() >= MAX_ATTEMPTS) {
                user.setLockUntil(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
                userRepository.save(user);
                logger.warn("Compte bloque apres {} echecs : {}", MAX_ATTEMPTS, email);
                throw new AccountLockedException("Compte bloque apres 5 echecs. Reessayez dans 2 minutes");
            }
            userRepository.save(user);
            logger.warn("Connexion echouee : mauvais mot de passe pour - {}", email);
            throw new AuthenticationFailedException("Email ou mot de passe incorrect");
        }

        // Reset les tentatives et générer un token
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);
        logger.info("Connexion reussie pour : {}", email);
        return token;
    }

    /**
     * Vérifie si un token est valide et retourne l'utilisateur associé.
     */
    public User getUserByToken(String token) {
        if (token == null || token.isBlank()) {
            throw new AuthenticationFailedException("Token manquant");
        }
        return userRepository.findByToken(token)
                .orElseThrow(() -> new AuthenticationFailedException("Token invalide"));
    }
}