package br.com.coeur.api.modules.users.presentation;

import br.com.coeur.api.modules.users.application.usecase.delete.DeleteUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "Usuários")
public class DeleteUsersController {

    private final DeleteUserUseCase useCase;

    public DeleteUsersController(DeleteUserUseCase useCase) {
        this.useCase = useCase;
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deleta um Usuário", description = "Exclui um usuário identificado pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        useCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
