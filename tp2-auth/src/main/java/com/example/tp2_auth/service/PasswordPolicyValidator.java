package com.example.tp2_auth.service;

import com.example.tp2_auth.exception.InvalidInputException;
import org.springframework.stereotype.Component;

/**
 * Validateur de politique de mot de passe.
 * Vérifie que le mot de passe respecte les règles de sécurité.
 */
@Component
public class PasswordPolicyValidator {

    /**
     * Valide le mot de passe selon la politique de sécurité.
     * Règles : 12 caractères min, 1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial.
     */
    public void validate(String password) {
        if (password == null || password.length() < 12) {
            throw new InvalidInputException("Le mot de passe doit contenir au moins 12 caractères");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidInputException("Le mot de passe doit contenir au moins une majuscule");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new InvalidInputException("Le mot de passe doit contenir au moins une minuscule");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new InvalidInputException("Le mot de passe doit contenir au moins un chiffre");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new InvalidInputException("Le mot de passe doit contenir au moins un caractère spécial");
        }
    }

    /**
     * Retourne la force du mot de passe : WEAK, MEDIUM ou STRONG.
     */
    public String getStrength(String password) {
        if (password == null || password.length() < 12) return "WEAK";
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        int score = 0;
        if (hasUpper) score++;
        if (hasLower) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;
        if (password.length() >= 16) score++;

        if (score <= 2) return "WEAK";
        if (score == 3) return "MEDIUM";
        return "STRONG";
    }
}