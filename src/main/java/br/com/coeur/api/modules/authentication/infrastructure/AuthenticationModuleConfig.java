package br.com.coeur.api.modules.authentication.infrastructure;

import br.com.coeur.api.modules.authentication.application.CookieSettings;
import br.com.coeur.api.modules.authentication.application.JwtSettings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtSettings.class, CookieSettings.class})
public class AuthenticationModuleConfig {
}
