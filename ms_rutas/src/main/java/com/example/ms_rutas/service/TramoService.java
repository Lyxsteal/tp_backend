package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camion;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.repository.CamionRepository;
import com.example.ms_rutas.repository.TramoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TramoService {
    private final TramoRepository tramoRepository;
    private final CamionRepository camionRepository;

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


    public Tramo actualizarEstadoTramo(Integer idTramo, String estadoTramo) {
        Tramo tramoExistente = obtenerTramoPorId(idTramo);
        tramoExistente.setEstadoTramo(estadoTramo);
        return tramoRepository.save(tramoExistente);
    }

    public Tramo asignarCamionATramo(Integer idTramo,String camionPatente) {
        Camion camion = camionRepository.findById(camionPatente)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con ID: " + camionPatente));
        Tramo tramo = obtenerTramoPorId(idTramo);
        tramo.setCamion(camion);
        return tramoRepository.save(tramo);
    }
    public List<Tramo> obtenerTramosPorCamionero(Integer idCamionero) {
        return tramoRepository.encontrarTramosCamionero(idCamionero);
    }
}