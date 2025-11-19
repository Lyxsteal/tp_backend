package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.Ubicacion;
import com.example.ms_rutas.repository.UbicacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UbicacionService {
    private final UbicacionRepository ubicacionRepository;
    private static final Logger log = LoggerFactory.getLogger(TramoService.class);
    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Transactional(readOnly = true)
    public List<Ubicacion> obtenerTodasLasUbicaciones() {
        log.info("buscando todas las ubicaciones");
        return ubicacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ubicacion obtenerUbicacionPorId(Integer id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ubicacino no encontrada");
                    return new RuntimeException("ubicacion no encontrado con id: " + id);
                });
    }

    @Transactional
    public Ubicacion crearUbicacion(Ubicacion ubicacion) {
        if (ubicacionRepository.existsById(ubicacion.getIdUbicacion())){
            log.warn("la ubicacion ya existe");
            throw new RuntimeException("la ubicacion con ID: "+ ubicacion.getIdUbicacion() + "ya existe");
        }
        log.info("creando ubicacion");
        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public Ubicacion actualizarUbicacion(Integer id, Ubicacion ubicacionActualizada) {
        Ubicacion ubicacionExistente = obtenerUbicacionPorId(id);
        ubicacionExistente.setDireccion(ubicacionActualizada.getDireccion());
        ubicacionExistente.setLatitud(ubicacionActualizada.getLatitud());
        ubicacionExistente.setLongitud(ubicacionActualizada.getLongitud());

        log.info("actualizando ubicacion");
        return ubicacionRepository.save(ubicacionExistente);
    }

    @Transactional
    public void eliminarUbicacion(Integer id) {
        if (!ubicacionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Ubicacion no encontrada con id: " + id);
        }
        log.info("borrando ubicacion con ID: " +id);
        ubicacionRepository.deleteById(id);
    }

}
