package br.com.coeur.api.modules.authentication.application.usecase.me;

import br.com.coeur.api.security.application.CurrentUser;
import br.com.coeur.api.shared.ApiException;
import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.UsersRepository;
import br.com.coeur.api.modules.users.domain.User;
import org.springframework.stereotype.Service;

@Service
public class MeUseCase {

    private static final String ERR_NOT_FOUND = "Usuário não encontrado.";

    private final UsersRepository repository;
    private final CurrentUser currentUser;

    public MeUseCase(UsersRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    public UserResponse execute() {
        User user = repository.findById(currentUser.getId()).orElseThrow(() -> ApiException.notFound(ERR_NOT_FOUND));

        return UserResponse.fromEntity(user);
    }
}
