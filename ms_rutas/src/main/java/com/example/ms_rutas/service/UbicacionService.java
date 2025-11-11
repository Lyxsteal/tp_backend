package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Ubicacion;
import com.example.ms_rutas.repository.UbicacionRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


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


}
