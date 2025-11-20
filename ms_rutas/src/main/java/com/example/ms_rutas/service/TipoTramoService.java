package com.example.ms_rutas.service;

import com.example.ms_rutas.model.TipoTramo;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.repository.TipoTramoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoTramoService {
    private final TipoTramoRepository tipoTramoRepository;
    private static final Logger log = LoggerFactory.getLogger(TipoTramoService.class);


    @Transactional(readOnly = true)
    public List<TipoTramo> obtenerTodosLosTiposTramo() {

        log.info("Obteniendo todos los tipos de tramo:\n" );
        return tipoTramoRepository.findAll();
    }
    @Transactional(readOnly = true)
    public TipoTramo obtenerTipoTramoPorId(Integer idTramo) {
        return tipoTramoRepository.findById(idTramo)
                .orElseThrow(() -> {
                    log.warn("Tramo con id " + idTramo + " no encontrado");
                    return new RuntimeException("Tipo tramo no encontrado con id: " + idTramo);
                });
    }

    @Transactional
    public TipoTramo crearTipoTramo(TipoTramo tipoTramo) {
        tipoTramo.setIdTipoTramo(null);
        log.info("Creando nuevo tipo de tramo...");
        return tipoTramoRepository.save(tipoTramo);
    }

    @Transactional
    public TipoTramo actualizarTipoTramo(Integer id, TipoTramo tipoTramoActualizado) {
        TipoTramo tipoTramoExistente = obtenerTipoTramoPorId(id);
        log.info("Actualizando tipo de tramo " + id);
        return tipoTramoRepository.save(tipoTramoExistente);
    }

    @Transactional
    public void eliminarTipoTramo(Integer id) {
        if (!tipoTramoRepository.existsById(id)) {
            log.warn("No se puede eliminar. No se pudo encontrar el tipo de tramo con id " + id);
            throw new RuntimeException("No se puede eliminar. Tramo no encontrado con id: " + id);
        }
        log.info("Eliminando tipo de tramo con id " + id);
        tipoTramoRepository.deleteById(id);
    }

}
