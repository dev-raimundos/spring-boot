package br.com.coeur.api.modules.users.presentation;

import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.usecase.update.UpdateUserRequest;
import br.com.coeur.api.modules.users.application.usecase.update.UpdateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "Usuários")
public class UpdateUsersController {

    private final UpdateUserUseCase useCase;

    public UpdateUsersController(UpdateUserUseCase useCase) {
        this.useCase = useCase;
    }

    @PutMapping("{id}")
    @Operation(summary = "Edita um Usuário", description = "Atualiza um usuário identificado pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        return useCase.execute(id, request);
    }
}
