package backend.grupo73.gateway.config;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(ex -> ex
                        // Dejamos libre el flujo de inicio de sesión. El filtro de login se encarga del callback.
                        .pathMatchers("/oauth2/**", "/login/**").permitAll()
                        // Proteger el endpoint local /gateway/whoami para cualquier usuario autenticado
                        .pathMatchers("/gateway/whoami").authenticated()
                        // proteger tus rutas detrás del gateway (poné roles si ya están en Keycloak)
                        .pathMatchers("/gateway/usuarios/**").hasAnyRole("ADMINISTRADOR","CLIENTE","TRANSPORTISTA")
                        .pathMatchers(HttpMethod.POST, "/api/v1/camiones").hasRole("TRANSPORTISTA")
                        //@Maxi hay q agregar todos los endpoints con rol y su respectivo metodo
                        .anyExchange().authenticated()
                )

                // habilitar flujo Authorization Code (redirige a Keycloak)
                .oauth2Login(Customizer.withDefaults())

                // validar JWT en las llamadas autenticadas
                .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object realmAccess = jwt.getClaim("realm_access");
            if (realmAccess instanceof Map<?,?> realmMap) {
                Object roles = realmMap.get("roles");
                if (roles instanceof Collection<?> list) {
                    return list.stream()
                            .map(Object::toString)
                            .map(r -> r.toUpperCase())
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                }
            }
            return Collections.emptyList();
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
