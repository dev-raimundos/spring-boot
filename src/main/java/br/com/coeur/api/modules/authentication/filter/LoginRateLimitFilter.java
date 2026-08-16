package br.com.coeur.api.modules.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final int PERMIT_LIMIT = 5;
    private static final Duration WINDOW = Duration.ofMinutes(1);
    private static final String MESSAGE = "Muitas tentativas. Tente novamente em 1 minuto.";

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    private record Window(AtomicInteger count, Instant resetAt) {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isLoginRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getRemoteAddr() != null ? request.getRemoteAddr() : "unknown";
        Instant now = Instant.now();

        Window window = windows.compute(key, (k, existing) -> {
            if (existing == null || now.isAfter(existing.resetAt())) {
                return new Window(new AtomicInteger(1), now.plus(WINDOW));
            }
            existing.count().incrementAndGet();
            return existing;
        });

        if (window.count().get() > PERMIT_LIMIT) {
            response.setStatus(429);
            response.setContentType("application/problem+json");
            response.getWriter().write(
                    "{\"type\":\"about:blank\",\"title\":\"Too Many Requests\",\"status\":429,\"detail\":\"" + MESSAGE
                            + "\",\"toast\":{\"type\":\"warning\",\"message\":\"" + MESSAGE + "\"}}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().endsWith("/api/v1/auth/login");
    }
}
