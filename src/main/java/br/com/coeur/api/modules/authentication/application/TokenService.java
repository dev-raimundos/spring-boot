package br.com.coeur.api.modules.authentication.application;

import br.com.coeur.api.modules.users.domain.User;

public interface TokenService {
    String generate(User user);
}
