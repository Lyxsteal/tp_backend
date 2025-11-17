package com.example.api_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Configuration
public class GatewayTokenFilter {

    @Bean
    public GatewayFilter userHeaderPropagationFilter() {

        return new OrderedGatewayFilter((exchange, chain) -> {
            System.out.println("Incoming Request URL: " + exchange.getRequest().getURI());
            return exchange.getPrincipal()
                    .flatMap(principal -> {

                        if (principal instanceof Authentication auth && auth.getPrincipal() instanceof Jwt jwt) {

                            String userId = jwt.getSubject();
                            String username = jwt.getClaimAsString("preferred_username");
                            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                            List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();

                            ServerWebExchange mutated = exchange.mutate()
                                    .request(req -> req.headers(headers -> {
                                        headers.add("X-User-Id", userId);
                                        headers.add("X-Username", username);
                                        headers.add("X-Roles", String.join(",", roles));
                                    }))
                                    .build();
                            System.out.println("Forwarding Request URL: " + mutated.getRequest().getURI());

                            return chain.filter(mutated);

                        }

                        return chain.filter(exchange);
                    });
        }, -1); // Ejecutar temprano
    }
}

