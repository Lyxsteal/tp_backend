package com.example.ms_rutas.service;

import com.example.ms_rutas.model.dto.OsrmResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DistanciaClient {
    private final RestTemplate restTemplate;
    public Double obtenerDistancia(String cooredenadas){
        String url = "http://osrm:5000/route/v1/driving/" + cooredenadas;
        return  restTemplate.getForObject(url, OsrmResponseDto.class).getRoutes().get(0).getDistance();
    }
}
