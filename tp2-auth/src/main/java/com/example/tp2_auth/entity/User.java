package com.example.tp2_auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant un utilisateur en base de données.
 * TP2 améliore le stockage, mais ne protège pas encore contre le rejeu.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "failed_attempts")
    private int failedAttempts = 0;

    @Column(name = "lock_until")
    private LocalDateTime lockUntil;

    private LocalDateTime createdAt;

    private String token;

    public User() {}

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public int getFailedAttempts() { return failedAttempts; }
    public LocalDateTime getLockUntil() { return lockUntil; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getToken() { return token; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }
    public void setLockUntil(LocalDateTime lockUntil) { this.lockUntil = lockUntil; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setToken(String token) { this.token = token; }
}