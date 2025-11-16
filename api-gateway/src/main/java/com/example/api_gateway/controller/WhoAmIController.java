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

        Mono<SecurityContext> contextMono = ReactiveSecurityContextHolder.getContext();

        Mono<Authentication> authMono = contextMono.map(securityContext -> securityContext.getAuthentication());

        return authMono.map(auth -> {

            Map<String, Object> out = new LinkedHashMap<>();
            out.put("authenticated", auth.isAuthenticated());
            out.put("authorities", auth.getAuthorities());
            out.put("name", auth.getName());

            Object p = auth.getPrincipal();

            if (p instanceof Jwt jwt) {
                out.put("principal_type", "Jwt");
                out.put("email", jwt.getClaim("email"));
                out.put("issuer", jwt.getIssuer());
            }
            else if (p instanceof OidcUser oidcUser) {
                out.put("principal_type", "OidcUser");
                out.put("email", oidcUser.getEmail());
                out.put("full_name", oidcUser.getFullName());
            }

            return out;
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