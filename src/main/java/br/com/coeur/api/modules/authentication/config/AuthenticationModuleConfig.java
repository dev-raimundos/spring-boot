package br.com.coeur.api.modules.authentication.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtSettings.class, CookieSettings.class})
public class AuthenticationModuleConfig {
}
