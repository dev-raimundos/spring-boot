package br.com.coeur.api.modules.users.presentation;

import br.com.coeur.api.shared.PagedResult;
import br.com.coeur.api.shared.Pagination;
import br.com.coeur.api.modules.users.application.UserResponse;
import br.com.coeur.api.modules.users.application.usecase.getall.GetAllUsersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "Usuários")
public class ListUsersController {

    private final GetAllUsersUseCase useCase;

    public ListUsersController(GetAllUsersUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    @Operation(summary = "Lista Usuários", description = "Retorna uma lista paginada de usuários.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de usuários"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public PagedResult<UserResponse> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        Pagination.Normalized normalized = Pagination.normalize(page, pageSize);
        return useCase.execute(normalized.page(), normalized.pageSize());
    }
}
