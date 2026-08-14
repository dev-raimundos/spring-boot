package br.com.coeur.api.security.application;

import br.com.coeur.api.shared.UserRole;

import java.util.UUID;

public interface CurrentUser {
    UUID getId();

    String getEmail();

    String getName();

    UserRole getRole();

    boolean isAdmin();

    boolean isAuthenticated();
}
