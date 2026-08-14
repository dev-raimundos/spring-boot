package br.com.coeur.api.modules.users.infrastructure;

import br.com.coeur.api.modules.users.application.UsersRepository;
import br.com.coeur.api.modules.users.domain.User;
import br.com.coeur.api.modules.users.infrastructure.persistence.UserEntity;
import br.com.coeur.api.modules.users.infrastructure.persistence.UserJpaRepository;
import br.com.coeur.api.modules.users.infrastructure.persistence.UserMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final UserJpaRepository jpaRepository;

    public UsersRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page findAll(int page, int pageSize) {
        var result = jpaRepository.findAllOrdered(PageRequest.of(page - 1, pageSize));

        List<User> items = result.getContent().stream()
                .map(UserMapper::toDomain)
                .toList();

        return new Page(items, result.getTotalElements());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void add(User user) {
        jpaRepository.save(UserMapper.toNewEntity(user));
    }

    @Override
    public void update(User user) {
        UserEntity entity = jpaRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado para atualização."));

        UserMapper.copyState(user, entity);
        jpaRepository.save(entity);
    }

    @Override
    public void delete(User user) {
        jpaRepository.deleteById(user.getId());
    }
}
