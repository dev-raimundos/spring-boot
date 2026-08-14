package br.com.coeur.api.modules.authentication.infrastructure;

import br.com.coeur.api.modules.authentication.application.JwtSettings;
import br.com.coeur.api.modules.authentication.application.TokenService;
import br.com.coeur.api.modules.users.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService implements TokenService {

    private final JwtSettings settings;

    public JwtTokenService(JwtSettings settings) {
        this.settings = settings;
    }

    @Override
    public String generate(User user) {
        SecretKey key = Keys.hmacShaKeyFor(settings.secret().getBytes(StandardCharsets.UTF_8));

        Instant now = Instant.now();
        Instant expiration = now.plus(settings.expirationHours(), ChronoUnit.HOURS);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("role", user.getRole().toString());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuer(settings.issuer())
                .audience().add(settings.audience()).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}
