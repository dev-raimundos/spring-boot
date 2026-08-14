package br.com.coeur.api.modules.users.application;

import br.com.coeur.api.modules.users.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository {

    record Page(List<User> items, long totalCount) {
    }

    Page findAll(int page, int pageSize);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void add(User user);

    void update(User user);

    void delete(User user);
}
