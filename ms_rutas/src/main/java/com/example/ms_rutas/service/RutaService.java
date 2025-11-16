package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.Ubicacion;
import com.example.ms_rutas.model.dto.CostoFinalDto;
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

    @Transactional
    public Ruta asignarTramosARuta(Integer idRuta, List<Tramo> tramos) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
        ruta.setTramos(tramos);
        return rutaRepository.save(ruta);
    }

    @Transactional
    public CostoFinalDto obtenerCostos(Integer idruta) {
        Ruta ruta = rutaRepository.findById(idruta);
        Double distanciaTotal = obtenerDistanciaTotal(ruta.getTramos());


        CostoFinalDto costo = new CostoFinalDto();
    }

    public Double obtenerDistanciaTotal(List<Tramo> tramos){
        Double distanciaTotal = 0.0;

        if (tramos == null || tramos.size() < 2) {
            return 0.0;
        }

        for (int i = 0; i < tramos.size() ; i++) {
            distanciaTotal += calcularDistancia(tramos.get(i));
        }

        return distanciaTotal;

    }
    public Double calcularDistancia(Tramo tramo){
        Ubicacion origen = tramo.getUbicacionOrigen();
        Ubicacion destino = tramo.getUbicacionDestino();
        
    }
}
