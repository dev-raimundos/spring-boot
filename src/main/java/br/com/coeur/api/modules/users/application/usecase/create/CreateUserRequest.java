package br.com.coeur.api.modules.users.application.usecase.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Nome é obrigatório.")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres.")
        String name,

        @NotBlank(message = "Email é obrigatório.")
        @Email(message = "Email inválido.")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres.")
        String email,

        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres.")
        String password
) {
}
