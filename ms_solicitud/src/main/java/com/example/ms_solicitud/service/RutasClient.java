package com.example.ms_solicitud.service;


import com.example.ms_solicitud.model.dto.CostoFinalDto;

import com.example.ms_solicitud.model.dto.RutaSugeridaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


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

    public List<RutaSugeridaDto> buscarRutastentativas(Integer idSolicitud) {
        // Usamos la URL base + el endpoint
        String url = msRutasBaseUrl + "/api/v1/rutas/rutas-tentativas/" + idSolicitud;
        RutaSugeridaDto[] rutasSugeridas = restTemplate.getForObject(url,  RutaSugeridaDto[].class);

        return Arrays.asList(rutasSugeridas);
    }
    public CostoFinalDto getCostos(Integer rutaId) {

        String url = msRutasBaseUrl + "/api/v1/rutas/costos/" + rutaId;

        log.info("buscando los costos");
        return restTemplate.getForObject(url, CostoFinalDto.class);
    }
    public Integer crearRuta(RutaSugeridaDto rutaSugerida, String coordenadasOrigen, String coordenadasDestino, Integer idSolicitud) {
        String url = msRutasBaseUrl + "/api/v1/rutas/" + "?coordenadasOrigen=" + coordenadasOrigen + "&coordenadasDestino=" + coordenadasDestino + "&idSolicitud=" + idSolicitud;
        Integer idRuta = restTemplate.postForObject(url, rutaSugerida, Integer.class);
        return idRuta;
    }
}