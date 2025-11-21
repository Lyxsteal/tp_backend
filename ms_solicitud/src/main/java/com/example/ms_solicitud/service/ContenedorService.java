package com.example.ms_solicitud.service;


import com.example.ms_solicitud.model.Contenedor;
import com.example.ms_solicitud.model.dto.ContenedorDto;
import com.example.ms_solicitud.repository.ContenedorRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContenedorService {
    private final ContenedorRepository contenedorRepository;
    private static final Logger log = LoggerFactory.getLogger(ContenedorService.class);
    public ContenedorService(ContenedorRepository contenedorRepository) {
        this.contenedorRepository = contenedorRepository;
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Contenedor> obtenerTodosLosContenedores() {
        return contenedorRepository.findAll();
    }

    @Transactional
    public Contenedor obtenerContenedorPorId(Integer idContenedor) {
        return contenedorRepository.findById(idContenedor)
                .orElseThrow(() -> {
                    log.warn("no se encontro el contenedor");
                   return new RuntimeException("Contenedor con id: " + idContenedor + " no encontrado.");
                });
    }

    @Transactional
    public Contenedor crearContenedor(ContenedorDto contenedorDto) {
        log.info("Creando contenedor...");
        Contenedor contenedor = new Contenedor();
        contenedor.setEstado("EN ORIGEN");
        contenedor.setPeso(contenedorDto.getPeso());
        contenedor.setVolumen(contenedorDto.getVolumen());
        return contenedorRepository.save(contenedor);
    }

    @Transactional
    public Contenedor actualizarContenedor(Integer idContenedor, Contenedor contenedorActualizado) {

        Contenedor contenedorExistente = obtenerContenedorPorId(idContenedor);
        contenedorExistente.setTiempoEstadia(contenedorActualizado.getTiempoEstadia());
        contenedorExistente.setEstado(contenedorActualizado.getEstado());
        contenedorExistente.setVolumen(contenedorActualizado.getVolumen());
        contenedorExistente.setPeso(contenedorActualizado.getPeso());

        log.info("Actualizando contenedor con id: "+ idContenedor);
        return contenedorRepository.save(contenedorExistente);
    }

    @Transactional
    public void eliminarContenedor(Integer idContenedor) {
        if (!contenedorRepository.existsById(idContenedor)) {
            log.warn("No existe el contenedor con id " + idContenedor);
            throw new RuntimeException("No se puede eliminar. Contenedor no encontrado con ID: " + idContenedor);
        }
        contenedorRepository.deleteById(idContenedor);
        log.info("Contenedor con ID: " + idContenedor + "eliminado exitosamente");
    }

    @Transactional
    public String obtenerEstado(Integer idContenedor) {
        Contenedor contenedor = contenedorRepository.findById(idContenedor).orElseThrow(() ->{
               log.warn("no existe el contenedor");
               return new RuntimeException("Contenedor con id: " + idContenedor + " no encontrado.");
        });
        return contenedor.getEstado();
    }

    @Transactional
    public List<Contenedor> obtenerPendientes(){
        log.info("Obteniendo contenedores con estado pendiente");
        return contenedorRepository.obtenerPendientes();
    }


}
