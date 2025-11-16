package com.example.ms_solicitud.service;


import com.example.ms_solicitud.model.dto.CostoFinalDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RutasClient {

    private final RestTemplate restTemplate;
    private final String msRutasBaseUrl; // Variable para la URL base

    // Inyectamos la URL desde las propiedades/variables de entorno
    public RutasClient(RestTemplate restTemplate,
                       @Value("${app.ms-rutas.base-url}") String msRutasBaseUrl) {
        this.restTemplate = restTemplate;
        this.msRutasBaseUrl = msRutasBaseUrl;
    }

    public boolean verificarRuta(Integer rutaId) {
        // Usamos la URL base + el endpoint
        String url = "http://ms_rutas:8085/api/v1/rutas/" + rutaId;

        try {
            restTemplate.getForObject(url, Object.class);
            return true; // si respondió, la ruta existe
        } catch (Exception e) {
            return false; // si devolvió 404 o error → no existe
        }
    }
    public CostoFinalDto getCostos(Integer rutaId) {
        // --- CÓDIGO CORREGIDO ---
        // 1. Usa la variable 'msRutasBaseUrl'
        // 2. Agrega el slash faltante entre "costos" y el ID
        String url = msRutasBaseUrl + "/api/v1/rutas/costos/" + rutaId;

        // Asumimos que la URL de arriba (ej. .../costos/123) existe en ms_rutas
        return restTemplate.getForObject(url, CostoFinalDto.class);
    }
}