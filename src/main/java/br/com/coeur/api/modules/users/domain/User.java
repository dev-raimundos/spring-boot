package br.com.coeur.api.modules.users.domain;

import br.com.coeur.api.shared.UserRole;

import java.time.Instant;
import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private final String email;
    private final String passwordHash;
    private final UserRole role;
    private final boolean active;
    private final boolean emailVerified;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;

    private User(
            UUID id,
            String name,
            String email,
            String passwordHash,
            UserRole role,
            boolean active,
            boolean emailVerified,
            int failedLoginAttempts,
            Instant lockedUntil,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLoginAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
        this.emailVerified = emailVerified;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lockedUntil = lockedUntil;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
    }

    public static User create(String name, String email, String passwordHash) {
        return new User(
                UUID.randomUUID(),
                name,
                email.toLowerCase(),
                passwordHash,
                UserRole.USER,
                true,
                false,
                0,
                null,
                Instant.now(),
                null,
                null
        );
    }

    public static User reconstitute(
            UUID id,
            String name,
            String email,
            String passwordHash,
            UserRole role,
            boolean active,
            boolean emailVerified,
            int failedLoginAttempts,
            Instant lockedUntil,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLoginAt
    ) {
        return new User(id, name, email, passwordHash, role, active, emailVerified, failedLoginAttempts,
                lockedUntil, createdAt, updatedAt, lastLoginAt);
    }

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(Instant.now());
    }

    public void updateProfile(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void recordLogin() {
        this.lastLoginAt = Instant.now();
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.lockedUntil = Instant.now().plusSeconds(15 * 60);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }
}
