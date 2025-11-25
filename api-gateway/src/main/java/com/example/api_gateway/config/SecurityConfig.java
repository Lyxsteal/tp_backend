package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        // ============================================================
                        // 1. ACCESO PÚBLICO
                        // ============================================================
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/inicio-sesion").permitAll()
                        .pathMatchers("/oauth2/**", "/login/**").permitAll()
                        .pathMatchers("/gateway/whoami").authenticated()

                        // ============================================================
                        // 2. REGLAS ESPECÍFICAS (Excepciones para Roles No-Admin)
                        // ============================================================

                        // --- Transportista ---
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/tramos/tramos-asignados/**").hasRole("TRANSPORTISTA")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/iniciar-tramo/*").hasRole("TRANSPORTISTA")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/finalizar-tramo/*").hasRole("TRANSPORTISTA")

                        // --- Cliente ---
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes/clientes").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/clientes/*").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/contenedores/estado/*").hasRole("CLIENTE")

                        // --- Compartidos (Admin + Otros) ---
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/{id}").hasAnyRole("ADMIN", "CLIENTE")
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes").hasAnyRole("ADMIN", "CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/costo-final/*").hasAnyRole("ADMIN", "CLIENTE")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/*").hasAnyRole("ADMIN", "TRANSPORTISTA")
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/rutas-tentativas").hasAnyRole("ADMIN", "CLIENTE")

                        // ============================================================
                        // 3. REGLAS GENERALES (Catch-All para ADMIN)
                        // ============================================================

                        // Todo lo demás de solicitudes (tarifas, contenedores, ABM, etc.) -> ADMIN
                        .pathMatchers("/api/v1/solicitudes/**").hasRole("ADMIN")

                        // Todo lo demás de rutas (camiones, depositos, calculos, ciudades) -> ADMIN
                        .pathMatchers("/api/v1/rutas/**").hasRole("ADMIN")

                        // Gestión de usuarios -> ADMIN
                        .pathMatchers("/api/v1/usuarios/**").hasRole("ADMIN")

                        // ============================================================
                        // 4. CIERRE (Cualquier otra cosa requiere login)
                        // ============================================================
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaim("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return Collections.emptyList();
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(roleName -> "ROLE_" + roleName.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}