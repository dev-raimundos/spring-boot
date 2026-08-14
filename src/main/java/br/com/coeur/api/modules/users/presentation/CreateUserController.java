package br.com.coeur.api.modules.users.presentation;

import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.usecase.create.CreateUserRequest;
import br.com.coeur.api.modules.users.application.usecase.create.CreateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "Usuários")
public class CreateUserController {

    private final CreateUserUseCase useCase;

    public CreateUserController(CreateUserUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @Operation(summary = "Cria um Usuário", description = "Criação de um usuário dado email, nome e senha.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "409", description = "Email já está em uso")
    })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = useCase.execute(request);
        return ResponseEntity.created(URI.create("api/v1/users/" + response.id())).body(response);
    }
}
