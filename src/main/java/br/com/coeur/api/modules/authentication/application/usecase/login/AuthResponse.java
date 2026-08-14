package br.com.coeur.api.modules.authentication.application.usecase.login;

import br.com.coeur.api.modules.users.application.UserResponse;

public record AuthResponse(
        UserResponse user,
        String token
) {
}
