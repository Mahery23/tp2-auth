package com.example.tp2_auth.exception;

/**
 * Exception levée quand une ressource existe déjà.
 */
public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(String message) {
        super(message);
    }
}