package com.example.ms_rutas.service;

import com.example.ms_rutas.model.*;
import com.example.ms_rutas.repository.*;
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
    private final RutaRepository rutaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final TipoTramoRepository tipoTramoRepository;


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
        Ruta ruta = rutaRepository.findById(tramo.getRuta().getIdRuta())
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
        tramo.setRuta(ruta);

        // 2. Cargar Ubicaciones
        Ubicacion origen = ubicacionRepository.findById(tramo.getUbicacionOrigen().getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación Origen no encontrada"));
        tramo.setUbicacionOrigen(origen);

        Ubicacion destino = ubicacionRepository.findById(tramo.getUbicacionDestino().getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación Destino no encontrada"));
        tramo.setUbicacionDestino(destino);

        // 3. Cargar TipoTramo
        TipoTramo tipo = tipoTramoRepository.findById(tramo.getTipoTramo().getIdTipoTramo())
                .orElseThrow(() -> new RuntimeException("Tipo de Tramo no encontrado"));
        tramo.setTipoTramo(tipo);
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
        camion.setDisponibilidad(false);
        Tramo tramo = obtenerTramoPorId(idTramo);
        tramo.setCamion(camion);
        return tramoRepository.save(tramo);
    }
    public List<Tramo> obtenerTramosPorCamionero(Integer idCamionero) {
        return tramoRepository.encontrarTramosCamionero(idCamionero);
    }
}