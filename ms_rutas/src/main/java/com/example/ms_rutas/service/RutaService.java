package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.Ubicacion;
import com.example.ms_rutas.model.dto.CostoFinalDto;
import com.example.ms_rutas.repository.RutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


import java.util.List;

@Service


@RequiredArgsConstructor
public class RutaService {
    private final RutaRepository rutaRepository;
    private final DistanciaClient distanciaClient;


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
        Ruta ruta = rutaRepository.findById(idruta)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
        Double consumo = obtenerConsumoTotal(ruta.getTramos());
        System.out.println(consumo);
        Integer cantDias = ruta.obtenerDiasEstadia();
        System.out.println(cantDias);
        CostoFinalDto costo = new CostoFinalDto();
        costo.setCantTramos(ruta.getTramos().size());
        costo.setConsumoTotalComb(consumo);
        costo.setDiasTotalEstadia(cantDias);
        return costo;
    }

    public Double obtenerConsumoTotal(List<Tramo> tramos){

        Double consumoTotal = 0.0;

        if (tramos == null || tramos.size() < 1) {
            return 0.0;
        }

        for (int i = 0; i < tramos.size() ; i++) {
            Double consumoCombustible = calcularDistancia(tramos.get(i)) * tramos.get(i).getCamion().getConsCombKm() / 1000;
            consumoTotal += consumoCombustible;

        }
        return consumoTotal;

    }
    public Double calcularDistancia(Tramo tramo){
        Ubicacion origen = tramo.getUbicacionOrigen();
        Ubicacion destino = tramo.getUbicacionDestino();

        String coordenadas = origen.getLongitud() + "," + origen.getLatitud() + ";"
                + destino.getLongitud() + "," + destino.getLatitud();

        Double distancia = distanciaClient.obtenerDistancia(coordenadas);

        System.out.println("distancia: " + distancia + " km");

        return distancia;

    }
}
