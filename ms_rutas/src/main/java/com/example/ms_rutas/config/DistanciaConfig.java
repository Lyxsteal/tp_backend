package com.example.ms_rutas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DistanciaConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}