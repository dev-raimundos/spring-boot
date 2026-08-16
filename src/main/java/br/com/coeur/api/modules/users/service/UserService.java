package br.com.coeur.api.modules.users.service;

import br.com.coeur.api.modules.users.dto.request.CreateUserRequest;
import br.com.coeur.api.modules.users.dto.request.UpdateUserRequest;
import br.com.coeur.api.modules.users.dto.response.UserResponse;
import br.com.coeur.api.modules.users.model.User;
import br.com.coeur.api.modules.users.repository.UserRepository;
import br.com.coeur.api.security.CurrentUserService;
import br.com.coeur.api.shared.ApiException;
import br.com.coeur.api.shared.PagedResult;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String ERR_NOT_FOUND = "Usuário não encontrado.";
    private static final String ERR_EMAIL_IN_USE = "Email já está em uso.";

    private final UserRepository repository;
    private final CurrentUserService currentUser;

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.email().toLowerCase())) {
            throw ApiException.conflict(ERR_EMAIL_IN_USE);
        }

        String passwordHash = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        User user = User.create(request.name(), request.email(), passwordHash);

        repository.save(user);

        return UserResponse.fromEntity(user);
    }

    public PagedResult<UserResponse> getAll(int page, int pageSize) {
        Page<User> result = repository.findAllOrdered(PageRequest.of(page - 1, pageSize));

        var items = result.getContent().stream()
                .map(UserResponse::fromEntity)
                .toList();

        return new PagedResult<>(items, page, pageSize, result.getTotalElements());
    }

    public UserResponse getById(UUID id) {
        requireSelfOrAdmin(id);

        return UserResponse.fromEntity(findByIdOrThrow(id));
    }

    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        requireSelfOrAdmin(id);

        User user = findByIdOrThrow(id);
        user.updateProfile(request.name());
        repository.save(user);

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public void delete(UUID id) {
        requireSelfOrAdmin(id);

        User user = findByIdOrThrow(id);
        repository.delete(user);
    }

    private User findByIdOrThrow(UUID id) {
        return repository.findById(id).orElseThrow(() -> ApiException.notFound(ERR_NOT_FOUND));
    }

    private void requireSelfOrAdmin(UUID id) {
        if (!id.equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw ApiException.forbidden();
        }
    }
}
