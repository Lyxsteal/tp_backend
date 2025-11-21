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

    public Double obtenerDistancia(String coordenadas) {
        log.info("Obteniendo distancia: ");
        String coordenadasLimpia = coordenadas.replace("\\s+", "").trim();
        String url = "http://osrm:5000/route/v1/driving/" + coordenadasLimpia;
        return restTemplate.getForObject(url, OsrmResponseDto.class).getRoutes().get(0).getDistance();
    }

    public OsrmRouteDto obtenerTiempoYDistancia(String coordenadas) {
        String coordenadasLimpia = coordenadas.replace("\\s+", "").trim();
        String url = "http://osrm:5000/route/v1/driving/" + coordenadasLimpia;
        OsrmResponseDto response = restTemplate.getForObject(url, OsrmResponseDto.class);
        if (response == null || response.getRoutes() == null || response.getRoutes().isEmpty()) {
            throw new RuntimeException("OSRM no encontró rutas válidas para las coordenadas proporcionadas.");
        }
        return restTemplate.getForObject(url, OsrmResponseDto.class).getRoutes().get(0);
    }
}
