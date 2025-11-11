package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camion;
import com.example.ms_rutas.model.Camionero;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ms_rutas.repository.CamionRepository;


import java.util.List;

@Service
public class CamionService {
    private final CamionRepository camionRepository;
    public CamionService(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }

    @Transactional(readOnly = true)
    public List<Camion> obtenerTodasLosCamiones() {
        return camionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Camion obtenerCamionPorPatente(String patente) {
        return camionRepository.findById(patente)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con ID: " + patente));
    }

    @Transactional
    public Camion crearCamion(Camion camion) {
        return camionRepository.save(camion);
    }

    @Transactional
    public Camion actualizarCamion(String patente, Camion camionActualizada) {
        Camion camionExistente = obtenerCamionPorPatente(patente);
        return camionRepository.save(camionExistente);
    }

    @Transactional
    public void eliminarCamion(String patente) {
        if (!camionRepository.existsById(patente)) {
            throw new RuntimeException("No se puede eliminar. Camion no encontrado con patente: " + patente);
        }
        camionRepository.deleteById(patente);
    }
}
