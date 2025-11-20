package com.example.ms_rutas.service;

import com.example.ms_rutas.model.dto.OsrmResponseDto;
import com.example.ms_rutas.model.dto.OsrmRouteDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DistanciaClient {
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(DistanciaClient.class);

    public Double obtenerDistancia(String cooredenadas) {
        log.info("obteniendo distancia: ");
        String url = "http://osrm:5000/route/v1/driving/" + cooredenadas;
        return restTemplate.getForObject(url, OsrmResponseDto.class).getRoutes().get(0).getDistance();
    }

    public OsrmRouteDto obtenerTiempoYDistancia(String coordenadas) {
        String url = "http://osrm:5000/trip/v1/driving/" + coordenadas;
        return restTemplate.getForObject(url, OsrmResponseDto.class).getRoutes().get(0);
    }
}
