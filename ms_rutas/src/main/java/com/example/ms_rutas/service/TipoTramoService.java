package com.example.ms_rutas.service;

import com.example.ms_rutas.model.TipoTramo;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.repository.TipoTramoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoTramoService {
    private final TipoTramoRepository tipoTramoRepository;

    @Transactional(readOnly = true)
    public List<TipoTramo> obtenerTodosLosTiposTramo() {
        return tipoTramoRepository.findAll();
    }
    @Transactional(readOnly = true)
    public TipoTramo obtenerTipoTramoPorId(Integer id) {
        return tipoTramoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo tramo no encontrado con id: " + id));
    }

    @Transactional
    public TipoTramo crearTipoTramo(TipoTramo tipoTramo) {
        return tipoTramoRepository.save(tipoTramo);
    }

    @Transactional
    public TipoTramo actualizarTipoTramo(Integer id, TipoTramo tipoTramoActualizado) {
        TipoTramo tipoTramoExistente = obtenerTipoTramoPorId(id);

        return tipoTramoRepository.save(tipoTramoExistente);
    }

    @Transactional
    public void eliminarTipoTramo(Integer id) {
        if (!tipoTramoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Tramo no encontrado con id: " + id);
        }
        tipoTramoRepository.deleteById(id);
    }

}
