package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.repository.CamioneroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CamioneroService {
    private final CamioneroRepository camioneroRepository;
    private static final Logger log = LoggerFactory.getLogger(CamioneroService.class);

    public CamioneroService(CamioneroRepository camioneroRepository) {
        this.camioneroRepository = camioneroRepository;
    }

    @Transactional(readOnly = true)
    public List<Camionero> obtenerTodosLosCamioneros() {
        log.info("Devolviendo todos los camioneros:");
        return camioneroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Camionero obtenerCamioneroPorCedula(Integer cedula) {
        return camioneroRepository.findById(cedula)
                .orElseThrow(() -> {
                    log.warn("No se encontró camionero con cédula " + cedula);
                    return new RuntimeException("Camionero no encontrado con cedula: " + cedula);
                });
    }

    @Transactional
    public Camionero crearCamionero(Camionero camionero) {
        log.info("Creando camionero...");
        return camioneroRepository.save(camionero);
    }

    @Transactional
    public Camionero actualizarCamionero(Integer cedula, Camionero camioneroActualizado) {
        Camionero camioneroExistente = obtenerCamioneroPorCedula(cedula);
        log.info("Actualizando camionero con cédula " + cedula);
        return camioneroRepository.save(camioneroExistente);
    }

    @Transactional
    public void eliminarCamionero(Integer cedula) {
        if (!camioneroRepository.existsById(cedula)) {
            log.warn("No se pudo eliminar camionero con cédula " + cedula);
            throw new RuntimeException("No se puede eliminar. Camionero no encontrada con cédula: " + cedula);
        }
        camioneroRepository.deleteById(cedula);
    }
}
