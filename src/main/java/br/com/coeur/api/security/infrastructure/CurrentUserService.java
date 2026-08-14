package br.com.coeur.api.security.infrastructure;

import br.com.coeur.api.security.CurrentUserPrincipal;
import br.com.coeur.api.security.application.CurrentUser;
import br.com.coeur.api.shared.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserService implements CurrentUser {

    private CurrentUserPrincipal principal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CurrentUserPrincipal principal)) {
            return null;
        }
        return principal;
    }

    @Override
    public UUID getId() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.id() : new UUID(0, 0);
    }

    @Override
    public String getEmail() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.email() : "";
    }

    @Override
    public String getName() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.name() : "";
    }

    @Override
    public UserRole getRole() {
        CurrentUserPrincipal principal = principal();
        return principal != null ? principal.role() : UserRole.USER;
    }

    @Override
    public boolean isAdmin() {
        return getRole() == UserRole.ADMIN;
    }

    @Override
    public boolean isAuthenticated() {
        return principal() != null;
    }
}
