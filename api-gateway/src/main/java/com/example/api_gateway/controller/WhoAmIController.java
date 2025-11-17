package com.example.api_gateway.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class WhoAmIController { // O en la clase que prefieras

    /**
     * Endpoint para verificar la identidad del usuario logueado.
     */
    @GetMapping("/gateway/whoami")
    public Mono<Map<String, Object>> whoami() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("authenticated", auth.isAuthenticated());
                    result.put("username", auth.getName());

                    Object principal = auth.getPrincipal();

                    if (principal instanceof Jwt jwt) {
                        result.put("type", "JWT");
                        result.put("email", jwt.getClaim("email"));
                        result.put("roles", jwt.getClaim("realm_access"));
                        result.put("expiresAt", jwt.getExpiresAt());
                    }

                    return result;
                });
    }


    @GetMapping("/gateway/whoami-corto")
    public Mono<Map<String, Object>> whoamiShort() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> {
                    Map<String, Object> out = new LinkedHashMap<>();
                    out.put("authenticated", auth.isAuthenticated());
                    out.put("authorities", auth.getAuthorities());
                    out.put("principal", auth.getPrincipal());
                    return out;
                });
    }
}