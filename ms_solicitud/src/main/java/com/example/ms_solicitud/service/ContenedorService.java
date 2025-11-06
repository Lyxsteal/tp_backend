package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Cliente;
import com.example.ms_solicitud.model.Contenedor;
import com.example.ms_solicitud.repository.ContenedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ContenedorService {
    private final ContenedorRepository contenedorRepository;
    public ContenedorService(ContenedorRepository contenedorRepository) {
        this.contenedorRepository = contenedorRepository;
    }

    @Transactional
    public Contenedor obtenerContenedorPorId(Integer idContenedor) {
        return contenedorRepository.findById(idContenedor)
                .orElseThrow(() -> new RuntimeException("Contenedor con id: " + idContenedor + " no encontrado."));
    }

    @Transactional
    public Contenedor crearContenedor(Contenedor contenedor) {
        return contenedorRepository.save(contenedor);
    }

    @Transactional
    public Contenedor actualizarContenedor(Integer idContenedor, Contenedor contenedorActualizado) {

        Contenedor contenedorExistente = obtenerContenedorPorId(idContenedor);

        contenedorExistente.setPeso(contenedorActualizado.getPeso());
        contenedorExistente.setVolumen(contenedorActualizado.getVolumen());
        contenedorExistente.setEstado(contenedorActualizado.getEstado());
        contenedorExistente.setTiempoEstadia(contenedorActualizado.getTiempoEstadia());

        return contenedorRepository.save(contenedorExistente);
    }

    @Transactional
    public void eliminarContenedor(Integer idContenedor) {
        if (!contenedorRepository.existsById(idContenedor)) {
            throw new RuntimeException("No se puede eliminar. Contenedor no encontrado con ID: " + idContenedor);
        }
        contenedorRepository.deleteById(idContenedor);
    }


}
