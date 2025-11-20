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

import java.util.List;

@Service
@RequiredArgsConstructor
public class TramoService {
    private final TramoRepository tramoRepository;
    private final CamionRepository camionRepository;
    private final RutaRepository rutaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final TipoTramoRepository tipoTramoRepository;
    private static final Logger log = LoggerFactory.getLogger(TramoService.class);


    @Transactional(readOnly = true)
    public List<Tramo> obtenerTodasLosTramos() {
        log.info("obteniendo todos los tramos");
        return tramoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tramo obtenerTramoPorId(Integer id) {
        return tramoRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("no se encontro el tramo");
                    return new RuntimeException("Tramo no encontrado con id: " + id);
                });
    }

    @Transactional
    public Tramo crearTramo(TramoDto tramoDto) {
        Tramo tramo = new Tramo();
        Ubicacion origen = ubicacionRepository.findById(tramoDto.getUbicacionOrigenId())
                .orElseThrow(() ->{
                    log.warn("Ubicacion de origen no encontrada");
                    return new RuntimeException("Ubicación Origen no encontrada");});
        log.info("Ubicacion de origen encontrada");
        tramo.setUbicacionOrigen(origen);

        Ubicacion destino = ubicacionRepository.findById(tramoDto.getUbicacionDestinoId())
                .orElseThrow(() -> {
                    log.warn("no se encontro la ubicacion de destino");
                    return new RuntimeException("Ubicación Destino no encontrada");
                });
        log.info("ubicacion destino encontrada");
        tramo.setUbicacionDestino(destino);

        // 3. Cargar TipoTramo
        TipoTramo tipo = tipoTramoRepository.findById(tramoDto.getTipoTramo())
                .orElseThrow(() -> {
                    log.warn("Tipo de tramo No encontrado");
                    return new RuntimeException("Tipo de Tramo no encontrado");
                });
        log.info("tipo de tramo encontrado");
        tramo.setTipoTramo(tipo);
        tramo.setEstadoTramo(tramoDto.getEstadoTramo());
        log.info("creando tramo");
        return tramoRepository.save(tramo);
    }

    @Transactional
    public Tramo actualizarTramo(Integer id, Tramo tramoActualizado) {
        Tramo tramoExistente = obtenerTramoPorId(id);
        tramoExistente.setEstadoTramo(tramoActualizado.getEstadoTramo());
        tramoExistente.setTipoTramo(tramoActualizado.getTipoTramo());
        tramoExistente.setCamion(tramoActualizado.getCamion());
        tramoExistente.setRuta(tramoExistente.getRuta());
        tramoExistente.setCostoAproximado(tramoExistente.getCostoAproximado());
        tramoExistente.setFechaHoraFin(tramoActualizado.getFechaHoraFin());
        tramoExistente.setFechaHoraInicio(tramoActualizado.getFechaHoraInicio());
        tramoExistente.setNroOrden(tramoActualizado.getNroOrden());
        tramoExistente.setUbicacionDestino(tramoActualizado.getUbicacionDestino());
        tramoExistente.setUbicacionOrigen(tramoActualizado.getUbicacionOrigen());
        log.info("Tramo Actualizado");
        return tramoRepository.save(tramoExistente);
    }

    @Transactional
    public void eliminarTramo(Integer id) {
        if (!tramoRepository.existsById(id)) {
            log.warn("No se pudo eliminar. El tramo no se encontro con id:"+id);
            throw new RuntimeException("No se puede eliminar. Tramo no encontrado con id: " + id);
        }
        log.info("Tramo eliminado");
        tramoRepository.deleteById(id);
    }


    public Tramo actualizarEstadoTramo(Integer idTramo, String estadoTramo) {
        Tramo tramoExistente = obtenerTramoPorId(idTramo);
        tramoExistente.setEstadoTramo(estadoTramo);
        return tramoRepository.save(tramoExistente);
    }

    public Tramo asignarCamionATramo(Integer idTramo,String camionPatente) {
        String patenteLimpia = camionPatente.trim().replace("\"", "");
        Camion camion = camionRepository.findById(patenteLimpia)
                .orElseThrow(() ->{
                    log.warn("Camion no encontrado con ID"+ camionPatente);
                    return new RuntimeException("Camion no encontrado con ID: " + camionPatente)
                ;});
        camion.setDisponibilidad(false);
        Tramo tramo = obtenerTramoPorId(idTramo);
        tramo.setCamion(camion);
        log.info("Tramo fue asignado a un camion con ID:"+ camionPatente);
        return tramoRepository.save(tramo);
    }
    public List<Tramo> obtenerTramosPorCamionero(Integer idCamionero) {
        log.info("Obtenidos tramos por camionero Id:"+idCamionero);
        return tramoRepository.encontrarTramosCamionero(idCamionero);
    }
}