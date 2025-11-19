package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.model.dto.CostoFinalDto;
import com.example.ms_solicitud.repository.TarifaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarifaService {
    private final TarifaRepository tarifaRepository;
    private static final Logger log = LoggerFactory.getLogger(SolicitudService.class);
    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    @Transactional(readOnly = true)
    public List<Tarifa> obtenerTodasLasTarifas() {

        log.info("buscando todas las tarifas");
        return tarifaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tarifa obtenerTarifaPorId(Integer id) {
        return tarifaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("no se encontro el contenedor");
                    return new RuntimeException("Tarifa no encontrada con ID: " + id);
                });
    }

    @Transactional
    public Tarifa crearTarifa(Tarifa tarifa) {
        // Aquí podrías agregar lógica de validación
        if(tarifaRepository.existsById(tarifa.getIdTarifa())) {
            log.warn("la tarifa ya existe");
            throw new RuntimeException("tarifa con ID; " + tarifa.getIdTarifa() + "ya existe");
        }
        log.info("creando tarifa");
        return tarifaRepository.save(tarifa);
    }

    @Transactional
    public Tarifa actualizarTarifa(Integer id, Tarifa tarifaActualizada) {
        Tarifa tarifaExistente = obtenerTarifaPorId(id);
        log.info("Actualizando tarifa " + id);
        return tarifaRepository.save(tarifaActualizada);
    }

    @Transactional
    public void eliminarTarifa(Integer id) {
        if (!tarifaRepository.existsById(id)) {
            log.warn("No se encontro la tarifa con id " + id);
            throw new RuntimeException("No se puede eliminar. Tarifa no encontrada con ID: " + id);
        }
        tarifaRepository.deleteById(id);
    }


}
