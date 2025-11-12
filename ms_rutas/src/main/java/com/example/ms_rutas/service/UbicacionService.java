package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Ubicacion;
import com.example.ms_rutas.repository.UbicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UbicacionService {
    private final UbicacionRepository ubicacionRepository;
    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Transactional
    public Ubicacion obtenerUbicacion(Integer idUbicacion) {
        return ubicacionRepository.findById(idUbicacion)
                .orElseThrow(() -> new RuntimeException("Ubicacion con id: " + idUbicacion + " no encontrada."));
    }

    @Transactional(readOnly = true)
    public Ubicacion obtenerUbicacionPorId(Integer id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ubicacion no encontrado con id: " + id));
    }

    @Transactional
    public Ubicacion crearUbicacion(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public Ubicacion actualizarUbicacion(Integer id, Ubicacion ubicacionActualizada) {
        Ubicacion ubicacionExistente = obtenerUbicacionPorId(id);
        ubicacionExistente.setDireccion(ubicacionActualizada.getDireccion());
        ubicacionExistente.setLatitud(ubicacionActualizada.getLatitud());
        ubicacionExistente.setLongitud(ubicacionActualizada.getLongitud());

        return ubicacionRepository.save(ubicacionExistente);
    }

    @Transactional
    public void eliminarUbicacion(Integer id) {
        if (!ubicacionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Ubicacion no encontrada con id: " + id);
        }
        ubicacionRepository.deleteById(id);
    }

}
