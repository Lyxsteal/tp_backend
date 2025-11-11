package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.repository.RutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RutaService {
    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @Transactional(readOnly = true)
    public List<Ruta> obtenerTodosLasRutas() {
        return rutaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ruta obtenerRutaPorId(Integer id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrado con id: " + id));
    }

    @Transactional
    public Ruta crearRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    @Transactional
    public Ruta actualizarRuta(Integer id, Ruta rutaActualizada) {
        Ruta rutaExistente = obtenerRutaPorId(id);
        return rutaRepository.save(rutaExistente);
    }

    @Transactional
    public void eliminarRuta(Integer id) {
        if (!rutaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Ruta no encontrada con id: " + id);
        }
        rutaRepository.deleteById(id);
    }
}
