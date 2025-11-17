package com.example.api_gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayTokenFilter {

    @Bean
    public WebFilter propagateUserHeaders() {
        return (exchange, chain) -> exchange.getPrincipal()
                .flatMap(principal -> {
                    if (principal instanceof Authentication auth && auth.getPrincipal() instanceof Jwt jwt) {

                        String userId = jwt.getSubject();
                        String username = jwt.getClaimAsString("preferred_username");
                        var roles = jwt.getClaimAsStringList("realm_access.roles");

                        // Evitar NPE
                        String rolesHeader = (roles == null || roles.isEmpty())
                                ? ""
                                : String.join(",", roles);

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .headers(httpHeaders -> {
                                    httpHeaders.remove(HttpHeaders.AUTHORIZATION); // No reenviar token

                                    if (userId != null)       httpHeaders.add("X-User-Id", userId);
                                    if (username != null)     httpHeaders.add("X-Username", username);
                                    if (!rolesHeader.isEmpty()) httpHeaders.add("X-Roles", rolesHeader);
                                })
                                .build();

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }

                    return chain.filter(exchange);
                });
    }
}


