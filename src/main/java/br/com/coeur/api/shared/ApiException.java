package br.com.coeur.api.shared;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final Map<String, String[]> errors;

    private ApiException(HttpStatus status, String message, Map<String, String[]> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }

    public static ApiException badRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message, null);
    }

    public static ApiException badRequest(String message, Map<String, String[]> errors) {
        return new ApiException(HttpStatus.BAD_REQUEST, message, errors);
    }

    public static ApiException unauthorized(String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, message, null);
    }

    public static ApiException unauthorized() {
        return unauthorized("Não autenticado.");
    }

    public static ApiException forbidden(String message) {
        return new ApiException(HttpStatus.FORBIDDEN, message, null);
    }

    public static ApiException forbidden() {
        return forbidden("Acesso negado.");
    }

    public static ApiException notFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND, message, null);
    }

    public static ApiException notFound() {
        return notFound("Recurso não encontrado.");
    }

    public static ApiException conflict(String message) {
        return new ApiException(HttpStatus.CONFLICT, message, null);
    }

    public static ApiException tooManyRequests(String message) {
        return new ApiException(HttpStatus.TOO_MANY_REQUESTS, message, null);
    }
}
