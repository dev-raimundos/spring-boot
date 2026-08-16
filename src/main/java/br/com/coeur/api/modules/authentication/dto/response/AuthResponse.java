package br.com.coeur.api.modules.authentication.dto.response;

import br.com.coeur.api.modules.users.dto.response.UserResponse;

public record AuthResponse(
        UserResponse user,
        String token
) {
}
