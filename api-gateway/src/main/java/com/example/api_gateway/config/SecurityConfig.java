package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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

                        // --- 1. RUTAS PÚBLICAS Y DE LOGIN ---
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/inicio-sesion").permitAll()
                        // (Rutas de callback de Keycloak)
                        .pathMatchers("/oauth2/**", "/login/**").permitAll()
                        .pathMatchers("/gateway/whoami").authenticated() // Tu endpoint de prueba

                        // --- 2. RUTAS DE TRANSPORTISTA  ---
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/tramos/tramos-asignados/**").hasRole("TRANSPORTISTA")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/iniciar-tramo/*").hasRole("TRANSPORTISTA")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/finalizar-tramo/*").hasRole("TRANSPORTISTA")

                        // --- 3. RUTAS DE CLIENTE ---
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes/clientes").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/clientes/*").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes").hasAnyRole("ADMIN", "CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/contenedores/estado/*").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/estimados/*").hasRole("CLIENTE") //get de costo y tiempo estimado
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/{id}").hasRole("CLIENTE")

                        // --- 4. RUTAS DE ADMIN (Específicas de Solicitud) ---
                        .pathMatchers(HttpMethod.GET, "/api/v1/solicitudes/contenedores/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/solicitudes/estados/*").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/solicitudes/contenedores/estado/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/solicitudes/costo-final/*").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes/costo-tiempo-real/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes/tarifas/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/solicitudes/tarifas/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/solicitudes/**").hasRole("ADMIN")

                        // --- 5. RUTAS DE ADMIN (Rutas, Camiones, Usuarios, etc.) ---
                        .pathMatchers(HttpMethod.GET, "/api/v1/deposito/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/deposito/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/rutas").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/tramos/sugeridos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/camion/**").hasRole("ADMIN") 
                        .pathMatchers(HttpMethod.POST, "/api/v1/rutas/depositos").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/depositos/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/rutas/camiones").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/camiones/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/camion/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/camiones/capacidad-maxima/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/camiones/camiones-aptos").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/camiones/costobase/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/camiones/consumo-prom/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/rutas/ciudades").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/rutas/ciudades").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/ciudades/*").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/usuarios").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/usuarios/*").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/*").hasRole("ADMIN")

                        // --- 6. RUTAS COMPARTIDAS (ADMIN + TRANSPORTISTA) ---
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rutas/tramos/*").hasAnyRole("ADMIN", "TRANSPORTISTA")

                        // --- 7. CIERRE ---
                        // (Cualquier otra ruta no definida aquí, requiere al menos estar autenticado)
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