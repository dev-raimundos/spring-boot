package br.com.coeur.api.modules.users.application.usecase.getbyid;

import br.com.coeur.api.security.application.CurrentUser;
import br.com.coeur.api.shared.ApiException;
import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.UsersRepository;
import br.com.coeur.api.modules.users.domain.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserByIdUseCase {

    private static final String ERR_NOT_FOUND = "Usuário não encontrado.";

    private final UsersRepository repository;
    private final CurrentUser currentUser;

    public GetUserByIdUseCase(UsersRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    public UserResponse execute(UUID id) {
        if (!id.equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw ApiException.forbidden();
        }

        User user = repository.findById(id).orElseThrow(() -> ApiException.notFound(ERR_NOT_FOUND));

        return UserResponse.fromEntity(user);
    }
}
