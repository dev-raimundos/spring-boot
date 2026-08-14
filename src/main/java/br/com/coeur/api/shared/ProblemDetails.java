package br.com.coeur.api.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Map;

public final class ProblemDetails {

    private ProblemDetails() {
    }

    public static ProblemDetail of(HttpStatus status, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setProperty("toast", Map.of("type", toastType(status), "message", detail));
        return problemDetail;
    }

    private static String toastType(HttpStatus status) {
        if (status.is5xxServerError()) {
            return "error";
        }
        if (status.is4xxClientError()) {
            return "warning";
        }
        return "info";
    }
}
