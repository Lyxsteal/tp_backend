package com.example.ms_solicitud.service;


import com.example.ms_solicitud.model.dto.CostoFinalDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class RutasClient {

    private final RestTemplate restTemplate;
    private final String msRutasBaseUrl; // Variable para la URL base
    private static final Logger log = LoggerFactory.getLogger(RutasClient.class);
    // Inyectamos la URL desde las propiedades/variables de entorno
    public RutasClient(RestTemplate restTemplate,
                       @Value("${APP_MS_RUTAS_BASE_URL}") String msRutasBaseUrl) {
        this.restTemplate = restTemplate;
        this.msRutasBaseUrl = msRutasBaseUrl;
    }

    public boolean verificarRuta(Integer rutaId) {
        // Usamos la URL base + el endpoint
        String url = msRutasBaseUrl + "/api/v1/rutas/" + rutaId;

        try {
            log.info("buascando ruta con ID: " + rutaId);
            restTemplate.getForObject(url, Object.class);
            log.info("ruta encontrada: ");
            return true; // si respondió, la ruta existe
        } catch (Exception e) {
            log.warn("no se encontro la ruta con id: " + rutaId);
            return false; // si devolvió 404 o error → no existe
        }
    }
    public CostoFinalDto getCostos(Integer rutaId) {

        String url = msRutasBaseUrl + "/api/v1/rutas/costos/" + rutaId;

        log.info("buscando los costos");
        return restTemplate.getForObject(url, CostoFinalDto.class);
    }
}