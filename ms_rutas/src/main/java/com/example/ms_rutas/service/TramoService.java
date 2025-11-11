package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.repository.TramoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TramoService {
    private final TramoRepository tramoRepository;

    public TramoService(TramoRepository tramoRepository) {
        this.tramoRepository = tramoRepository;
    }

    @Transactional(readOnly = true)
    public List<Tramo> obtenerTodasLosTramos() {
        return tramoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tramo obtenerTramoPorId(Integer id) {
        return tramoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con id: " + id));
    }

    @Transactional
    public Tramo crearTramo(Tramo tramo) {
        return tramoRepository.save(tramo);
    }

    @Transactional
    public Tramo actualizarTramo(Integer id, Tramo tramoActualizado) {
        Tramo tramoExistente = obtenerTramoPorId(id);

        return tramoRepository.save(tramoExistente);
    }

    @Transactional
    public void eliminarTramo(Integer id) {
        if (!tramoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Tramo no encontrado con id: " + id);
        }
        tramoRepository.deleteById(id);
    }
}