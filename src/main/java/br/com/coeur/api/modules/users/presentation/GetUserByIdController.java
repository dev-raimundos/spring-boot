package br.com.coeur.api.modules.users.presentation;

import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.usecase.getbyid.GetUserByIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "Usuários")
public class GetUserByIdController {

    private final GetUserByIdUseCase useCase;

    public GetUserByIdController(GetUserByIdUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("{id}")
    @Operation(summary = "Encontra um Usuário", description = "Retorna um usuário identificado pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public UserResponse getById(@PathVariable UUID id) {
        return useCase.execute(id);
    }
}
