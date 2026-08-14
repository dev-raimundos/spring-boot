package br.com.coeur.api.modules.authentication.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtSettings(
        String secret,
        String issuer,
        String audience,
        int expirationHours
) {
}
