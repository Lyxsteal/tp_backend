package com.example.ms_rutas.service;

import com.example.ms_rutas.model.dto.DatosSolicitudDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SolicitudClient {
    private final RestTemplate restTemplate;
    private final String msSolicitudBaseUrl;
    private static final Logger log = LoggerFactory.getLogger(SolicitudClient.class);
    public SolicitudClient(RestTemplate restTemplate,@Value("${APP_MS_SOLICITUD_BASE_URL}") String msSolicitudBaseUrl) {
        this.restTemplate = restTemplate;
        this.msSolicitudBaseUrl = msSolicitudBaseUrl;
    }
    public boolean verificarSolicitud(Integer solicitudId) {
        String url = msSolicitudBaseUrl + "/api/v1/solicitudes/" + solicitudId;

        try {
            log.info("Buscando solicitud con id: " + solicitudId);
            restTemplate.getForObject(url, Object.class);
            log.info("Ruta encontrada: ");
            return true;
        } catch (Exception e) {
            log.warn("No se encontro la ruta con id: " + solicitudId);
            return false; // si devolvió 404 o error → no existe
        }
    }
    public DatosSolicitudDto obtenerDatosSolicitudPorNumero(Integer idSolicitud) {
        String url = msSolicitudBaseUrl + "api/v1/solicitudes/datos-solicitud/" + idSolicitud;
        return restTemplate.getForObject(url, DatosSolicitudDto.class);
    }

    public void iniciarSolicitud(Integer idSolicitud) {
        String url = msSolicitudBaseUrl + "api/v1/solicitudes/iniciar-solicitud/" + idSolicitud;
        restTemplate.put(url, idSolicitud);

    }

    public void reanudarSolicitud(Integer idSolicitud) {
        String url = msSolicitudBaseUrl + "api/v1/solicitudes/reanudar-viaje/" + idSolicitud;
        restTemplate.put(url,idSolicitud);
    }

    public void ponerEnDeposito(Integer idSolicitud) {
        String url = msSolicitudBaseUrl + "api/v1/solicitudes/en-deposito/" + idSolicitud;
        restTemplate.put(url,idSolicitud);
    }
    public void finalizarSolicitud(Integer idSolicitud) {
        String url = msSolicitudBaseUrl + "api/v1/solicitudes/finalizar-solicitud/" + idSolicitud;
        restTemplate.put(url,idSolicitud);
    }


}
