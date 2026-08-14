package br.com.coeur.api.modules.authentication.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cookie")
public record CookieSettings(
        String domain
) {
}
