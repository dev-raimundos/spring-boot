package br.com.coeur.api.modules.users.application.usecase.getall;

import br.com.coeur.api.shared.PagedResult;
import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllUsersUseCase {

    private final UsersRepository repository;

    public GetAllUsersUseCase(UsersRepository repository) {
        this.repository = repository;
    }

    public PagedResult<UserResponse> execute(int page, int pageSize) {
        UsersRepository.Page result = repository.findAll(page, pageSize);

        List<UserResponse> items = result.items().stream()
                .map(UserResponse::fromEntity)
                .toList();

        return new PagedResult<>(items, page, pageSize, result.totalCount());
    }
}
