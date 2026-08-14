package br.com.coeur.api.modules.authentication.presentation;

import br.com.coeur.api.modules.authentication.application.usecase.me.MeUseCase;
import br.com.coeur.api.modules.users.application.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Perfil do Usuário Logado")
public class MeController {

    private final MeUseCase useCase;

    public MeController(MeUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("me")
    @Operation(summary = "Dados do usuário autenticado", description = "Retorna os dados completos do usuário dono do token JWT (cookie ou header Authorization).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário autenticado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public UserResponse me() {
        return useCase.execute();
    }
}
