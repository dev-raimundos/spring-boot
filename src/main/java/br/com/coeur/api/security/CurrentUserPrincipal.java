package br.com.coeur.api.security;

import br.com.coeur.api.shared.UserRole;

import java.util.UUID;

public record CurrentUserPrincipal(
        UUID id,
        String email,
        String name,
        UserRole role
) {
}
