package br.com.coeur.api.modules.authentication.application.usecase.login;

import br.com.coeur.api.modules.authentication.application.TokenService;
import br.com.coeur.api.shared.ApiException;
import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.UsersRepository;
import br.com.coeur.api.modules.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private static final String ERR_INVALID_CREDENTIALS = "Email ou senha inválidos.";
    private static final String ERR_ACCOUNT_LOCKED = "Conta temporariamente bloqueada por excesso de tentativas. Tente novamente mais tarde.";
    private static final String ERR_ACCOUNT_INACTIVE = "Conta desativada.";

    private final UsersRepository repository;
    private final TokenService tokenService;

    @Transactional
    public AuthResponse execute(LoginRequest request) {
        User user = repository.findByEmail(request.email().toLowerCase()).orElse(null);

        if (user == null || user.getPasswordHash() == null) {
            throw ApiException.unauthorized(ERR_INVALID_CREDENTIALS);
        }

        if (user.isLocked()) {
            throw ApiException.forbidden(ERR_ACCOUNT_LOCKED);
        }

        if (!BCrypt.checkpw(request.password(), user.getPasswordHash())) {
            user.recordFailedLogin();
            repository.update(user);
            throw ApiException.unauthorized(ERR_INVALID_CREDENTIALS);
        }

        if (!user.isActive()) {
            throw ApiException.forbidden(ERR_ACCOUNT_INACTIVE);
        }

        user.recordLogin();
        repository.update(user);

        String token = tokenService.generate(user);
        return new AuthResponse(UserResponse.fromEntity(user), token);
    }
}
