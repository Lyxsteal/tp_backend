package com.example.ms_rutas.service;

import com.example.ms_rutas.model.*;
import com.example.ms_rutas.model.dto.TramoDto;
import com.example.ms_rutas.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TramoService {
    private final TramoRepository tramoRepository;
    private final CamionRepository camionRepository;

    private final SolicitudClient solicitudClient;
    private static final Logger log = LoggerFactory.getLogger(TramoService.class);


    @Transactional(readOnly = true)
    public List<Tramo> obtenerTodasLosTramos() {
        log.info("Obteniendo todos los tramos");
        return tramoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tramo obtenerTramoPorId(Integer id) {
        return tramoRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("No se encontro el tramo");
                    return new RuntimeException("Tramo no encontrado con id: " + id);
                });
    }

    @Transactional
    public Tramo crearTramo(TramoDto tramoDto) {
        Tramo tramo = new Tramo();

       tramo.setCoordenadasOrigen(tramoDto.getCoordenadasOrigen());
       tramo.setCoordenadasDestino(tramoDto.getCoordenadasDestino());
        tramo.setTipoTramo(tramoDto.getTipoTramo());
        tramo.setEstadoTramo(tramoDto.getEstadoTramo());
        log.info("Creando tramo");
        return tramoRepository.save(tramo);
    }

    @Transactional
    public Tramo actualizarTramo(Integer id, Tramo tramoActualizado) {
        Tramo tramoExistente = obtenerTramoPorId(id);
        tramoExistente.setEstadoTramo(tramoActualizado.getEstadoTramo());
        tramoExistente.setTipoTramo(tramoActualizado.getTipoTramo());
        tramoExistente.setCamion(tramoActualizado.getCamion());
        tramoExistente.setRuta(tramoExistente.getRuta());
        tramoExistente.setFechaHoraFin(tramoActualizado.getFechaHoraFin());
        tramoExistente.setFechaHoraInicio(tramoActualizado.getFechaHoraInicio());
        tramoExistente.setNroOrden(tramoActualizado.getNroOrden());
        log.info("Tramo Actualizado");
        return tramoRepository.save(tramoExistente);
    }

    @Transactional
    public void eliminarTramo(Integer id) {
        if (!tramoRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Tramo no encontrado con id: "+id);
            throw new RuntimeException("No se puede eliminar. Tramo no encontrado con id: " + id);
        }
        log.info("Tramo eliminado");
        tramoRepository.deleteById(id);
    }

    @Transactional
    public Tramo iniciarTramo(Integer idTramo) {
        Tramo tramoAIniciar = obtenerTramoPorId(idTramo);
        Integer nroOrden = tramoAIniciar.getNroOrden();
        Integer idSolicitud = tramoAIniciar.getRuta().getIdSolicitud();

        if (tramoAIniciar.getNroOrden() == 1) {
            tramoAIniciar.setEstadoTramo("INICIADO");

            solicitudClient.iniciarSolicitud(idSolicitud);
        }
        else {
            Tramo tramoAnterior = tramoRepository.encontrarTramoPorNroOrden(tramoAIniciar.getNroOrden() - 1, tramoAIniciar.getRuta().getIdRuta());
            if (tramoAnterior.getEstadoTramo().equals("FINALIZADO")){
                tramoAIniciar.setEstadoTramo("INICIADO");
                solicitudClient.reanudarSolicitud(idSolicitud);
            }
            else {
                log.error("El tramo anterior aún no finalizó");
                throw new RuntimeException("El tramo anterior no finalizó");

            }
        }
        tramoAIniciar.setFechaHoraInicio(LocalDate.now());
        log.info("Tramo con id: " + idTramo + " cambiado a estado INICIAR");
        return tramoRepository.save(tramoAIniciar);
    }

    @Transactional
    public Tramo finalizarTramo(Integer idTramo) {
        Tramo tramoAFinalizar = obtenerTramoPorId(idTramo);
        Integer idSolicitud = tramoAFinalizar.getRuta().getIdSolicitud();
        if (tramoAFinalizar.getEstadoTramo().equals("INICIADO")) {
            if (tramoAFinalizar.getNroOrden() < tramoAFinalizar.getRuta().getTramos().size()) {
                log.info("Tramo con id: " + idTramo + " cambiado a estado FINALIZADO");
                solicitudClient.ponerEnDeposito(idSolicitud);
            }
            else {
                solicitudClient.finalizarSolicitud(idSolicitud);

            }

        }else{
            log.warn("No se pudo finalizar. Tramo no está iniciado");
            throw new RuntimeException("Tramo no esta iniciado");
        }
        tramoAFinalizar.setEstadoTramo("FINALIZADO");
        tramoAFinalizar.setFechaHoraFin(LocalDate.now());
        return tramoRepository.save(tramoAFinalizar);
    }

    public Tramo asignarCamionATramo(Integer idTramo,String camionPatente) {
        String patenteLimpia = camionPatente.trim().replace("\"", "");
        Camion camion = camionRepository.findById(patenteLimpia)
                .orElseThrow(() ->{
                    log.warn("Camion no encontrado con patente: "+ camionPatente);
                    return new RuntimeException("Camion no encontrado con patente: " + camionPatente)
                ;});
        camion.setDisponibilidad(false);
        Tramo tramo = obtenerTramoPorId(idTramo);
        tramo.setCamion(camion);
        log.info("Tramo fue asignado a un camion con patente: "+ camionPatente);
        return tramoRepository.save(tramo);
    }
    public List<Tramo> obtenerTramosPorCamionero(Integer idCamionero) {
        log.info("Obtenidos tramos por camionero id :"+idCamionero);
        return tramoRepository.encontrarTramosCamionero(idCamionero);
    }
}