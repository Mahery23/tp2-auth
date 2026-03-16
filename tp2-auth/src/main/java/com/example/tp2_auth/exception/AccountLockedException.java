package com.example.tp2_auth.exception;

/**
 * Exception levée quand un compte est temporairement bloqué.
 */
public class AccountLockedException extends RuntimeException {
    public AccountLockedException(String message) {
        super(message);
    }
}