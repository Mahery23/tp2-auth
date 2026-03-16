package com.example.tp2_auth.exception;

/**
 * Exception levée quand l'authentification échoue.
 */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}