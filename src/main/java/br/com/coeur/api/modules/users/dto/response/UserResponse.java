package br.com.coeur.api.modules.users.dto.response;

import br.com.coeur.api.modules.users.model.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String role,
        boolean isActive,
        boolean isEmailVerified,
        Instant createdAt,
        Instant updatedAt,
        Instant lastLoginAt
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().toString(),
                user.isActive(),
                user.isEmailVerified(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLoginAt()
        );
    }
}
