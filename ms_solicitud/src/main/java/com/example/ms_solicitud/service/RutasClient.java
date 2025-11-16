package com.example.ms_solicitud.service;


import com.example.ms_solicitud.model.dto.CostoFinalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RutasClient {

    private final RestTemplate restTemplate; // lo inyectamos

    public boolean verificarRuta(Integer rutaId) {
        String url = "http://localhost:8085/api/v1/rutas/" + rutaId; // o el host real del ms_rutas

        try {
            restTemplate.getForObject(url, Object.class);
            return true; // si respondió, la ruta existe
        } catch (Exception e) {
            return false; // si devolvió 404 o error → no existe
        }
    }
    public CostoFinalDto getCostos(Integer rutaId) {
        String url = "http://localhost:8085/api/v1/rutas/costos" + rutaId;
        return restTemplate.getForObject(url, CostoFinalDto.class);
    }
}