package br.com.coeur.api.modules.users.application.usecase.create;

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
public class CreateUserUseCase {

    private static final String ERR_EMAIL_IN_USE = "Email já está em uso.";

    private final UsersRepository repository;

    @Transactional
    public UserResponse execute(CreateUserRequest request) {
        if (repository.existsByEmail(request.email().toLowerCase())) {
            throw ApiException.conflict(ERR_EMAIL_IN_USE);
        }

        String passwordHash = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        User user = User.create(request.name(), request.email(), passwordHash);

        repository.add(user);

        return UserResponse.fromEntity(user);
    }
}
