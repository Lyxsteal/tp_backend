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

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .headers(httpHeaders -> {
                                    httpHeaders.remove(HttpHeaders.AUTHORIZATION); // ❌ remover token
                                    httpHeaders.add("X-User-Id", userId);
                                    httpHeaders.add("X-Username", username);
                                    httpHeaders.add("X-Roles", String.join(",", roles));
                                })
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                        return chain.filter(mutatedExchange);
                    }

                    return chain.filter(exchange);
                });
    }
}

