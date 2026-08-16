package br.com.coeur.api.modules.authentication.service;

import br.com.coeur.api.modules.authentication.dto.request.LoginRequest;
import br.com.coeur.api.modules.authentication.dto.response.AuthResponse;
import br.com.coeur.api.modules.users.dto.response.UserResponse;
import br.com.coeur.api.modules.users.model.User;
import br.com.coeur.api.modules.users.repository.UserRepository;
import br.com.coeur.api.security.CurrentUserService;
import br.com.coeur.api.shared.ApiException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ERR_INVALID_CREDENTIALS = "Email ou senha inválidos.";
    private static final String ERR_ACCOUNT_LOCKED = "Conta temporariamente bloqueada por excesso de tentativas. Tente novamente mais tarde.";
    private static final String ERR_ACCOUNT_INACTIVE = "Conta desativada.";
    private static final String ERR_NOT_FOUND = "Usuário não encontrado.";

    private final UserRepository repository;
    private final JwtService jwtService;
    private final CurrentUserService currentUser;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.email().toLowerCase()).orElse(null);

        if (user == null || user.getPasswordHash() == null) {
            throw ApiException.unauthorized(ERR_INVALID_CREDENTIALS);
        }

        if (user.isLocked()) {
            throw ApiException.forbidden(ERR_ACCOUNT_LOCKED);
        }

        if (!BCrypt.checkpw(request.password(), user.getPasswordHash())) {
            user.recordFailedLogin();
            repository.save(user);
            throw ApiException.unauthorized(ERR_INVALID_CREDENTIALS);
        }

        if (!user.isActive()) {
            throw ApiException.forbidden(ERR_ACCOUNT_INACTIVE);
        }

        user.recordLogin();
        repository.save(user);

        String token = jwtService.generate(user);
        return new AuthResponse(UserResponse.fromEntity(user), token);
    }

    public UserResponse getCurrentUser() {
        User user = repository.findById(currentUser.getId())
                .orElseThrow(() -> ApiException.notFound(ERR_NOT_FOUND));

        return UserResponse.fromEntity(user);
    }
}
