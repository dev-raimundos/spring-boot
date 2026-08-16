package br.com.coeur.api.security;

import br.com.coeur.api.shared.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserService {

    private CurrentUserPrincipal principal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CurrentUserPrincipal principal)) {
            return null;
        }
        return principal;
    }

    public UUID getId() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.id() : new UUID(0, 0);
    }

    public String getEmail() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.email() : "";
    }

    public String getName() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.name() : "";
    }

    public UserRole getRole() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.role() : UserRole.USER;
    }

    public boolean isAdmin() {
        return getRole() == UserRole.ADMIN;
    }

    public boolean isAuthenticated() {
        return principal() != null;
    }
}
