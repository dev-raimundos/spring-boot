package br.com.coeur.api.modules.authentication.controller;

import br.com.coeur.api.modules.authentication.config.CookieSettings;
import br.com.coeur.api.modules.authentication.dto.request.LoginRequest;
import br.com.coeur.api.modules.authentication.dto.response.AuthResponse;
import br.com.coeur.api.modules.authentication.service.AuthService;
import br.com.coeur.api.modules.users.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthService authService;
    private final CookieSettings cookieSettings;

    public AuthController(AuthService authService, CookieSettings cookieSettings) {
        this.authService = authService;
        this.cookieSettings = cookieSettings;
    }

    @PostMapping("login")
    @Operation(summary = "Login com email e senha", description = "Valida as credenciais e devolve o token JWT da API.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login efetuado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "403", description = "Conta bloqueada ou desativada"),
            @ApiResponse(responseCode = "429", description = "Muitas tentativas")
    })
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);

        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(authResponse.token(), Duration.ofHours(6)).toString());

        return ResponseEntity.ok(authResponse.user());
    }

    @PostMapping("logout")
    @Operation(summary = "Logout", description = "Expira o cookie HttpOnly do token.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logout efetuado")
    })
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie("", Duration.ZERO).toString());

        return ResponseEntity.noContent().build();
    }

    private ResponseCookie buildCookie(String value, Duration maxAge) {
        return ResponseCookie.from("token", value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .domain(cookieDomain())
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    private String cookieDomain() {
        return (cookieSettings.domain() == null || cookieSettings.domain().isBlank()) ? null : cookieSettings.domain();
    }
}
