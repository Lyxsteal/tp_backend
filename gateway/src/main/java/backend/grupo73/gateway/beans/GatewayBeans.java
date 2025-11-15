package backend.grupo73.gateway.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayBeans {

    @Bean
    public RouteLocator configuradorDeRutas(
        RouteLocatorBuilder builder,
        @Value("${server.uri.usuarios}") String uriUsuarios
    )
    {
        return builder.routes()
            .route(r -> r
                .path("/api/usuarios/**")
                .uri(uriUsuarios)
            )
            .build();
    }

}
