package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.Ubicacion;
import com.example.ms_rutas.model.dto.CostoFinalDto;
import com.example.ms_rutas.model.dto.OsrmRouteDto;
import com.example.ms_rutas.model.dto.RutaSugeridaDto;
import com.example.ms_rutas.repository.RutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service


@RequiredArgsConstructor
public class RutaService {
    private final RutaRepository rutaRepository;
    private final DistanciaClient distanciaClient;
    private static final Logger log = LoggerFactory.getLogger(RutaService.class);

    @Transactional(readOnly = true)
    public List<Ruta> obtenerTodosLasRutas() {
        log.info("Obtenidas todas las rutas");
        return rutaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ruta obtenerRutaPorId(Integer id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("la ruta no existe con id:"+ id);
                    return new RuntimeException("Ruta no encontrado con id: " + id);
                });
    }

    @Transactional
    public Ruta crearRuta(Ruta ruta) {
        if (rutaRepository.existsById(ruta.getIdRuta())){
            log.warn("ya existe la Ruta con ID: " + ruta.getIdRuta());
            throw new RuntimeException("ya existe la ruta");
        };
        log.info("creando ruta");
        return rutaRepository.save(ruta);
    }

    @Transactional
    public Ruta actualizarRuta(Integer id, Ruta rutaActualizada) {
        Ruta rutaExistente = obtenerRutaPorId(id);
        rutaExistente.setTramos(rutaActualizada.getTramos());
        rutaExistente.setCantidadTramos(rutaActualizada.getCantidadTramos());
        rutaExistente.setCantidadDepositos(rutaActualizada.getCantidadDepositos());
        log.info("Ruta actualizada");
        return rutaRepository.save(rutaExistente);
    }

    @Transactional
    public void eliminarRuta(Integer id) {
        if (!rutaRepository.existsById(id)) {
            log.warn("No se puedo eliminar.La ruta no se encontro con id:"+id);
            throw new RuntimeException("No se puede eliminar. Ruta no encontrada con id: " + id);
        }
        log.info("Ruta eliminada");
        rutaRepository.deleteById(id);
    }

    @Transactional
    public Ruta asignarTramosARuta(Integer idRuta, List<Tramo> tramos) {
        Ruta ruta = obtenerRutaPorId(idRuta);
        ruta.setTramos(tramos);
        return rutaRepository.save(ruta);
    }

    @Transactional

    public CostoFinalDto obtenerCostos(Integer idruta) {
        Ruta ruta = obtenerRutaPorId(idruta);
        log.info("calculando el consumo de combustible");
        Double consumo = obtenerConsumoTotal(ruta.getTramos());
        log.info("calculando la cantidad de dias");
        Integer cantDias = ruta.obtenerDiasEstadia();
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
        log.info("El consumo total es " + consumoTotal);
        return consumoTotal;

    }
    public Double calcularDistancia(Tramo tramo){
        Ubicacion origen = tramo.getUbicacionOrigen();
        Ubicacion destino = tramo.getUbicacionDestino();

        String coordenadas = origen.getLongitud() + "," + origen.getLatitud() + ";"
                + destino.getLongitud() + "," + destino.getLatitud();

        Double distancia = distanciaClient.obtenerDistancia(coordenadas);

        log.info("Distancia calculada");
        return distancia;

    }

    public List<RutaSugeridaDto> consultarRutasTentativas(List<Ruta> rutas){
        List<RutaSugeridaDto> rutaSugeridas = new ArrayList<>();
        log.info("calculando distancia de rutas tentativas");
        for (Ruta ruta : rutas) {
           Double distanciaTotal = 0.0;
           Double DuracionTotal = 0.0;
           for (Tramo tramo : ruta.getTramos()) {
               Ubicacion origen = tramo.getUbicacionOrigen();
               Ubicacion destino = tramo.getUbicacionDestino();
               String coordenadas = origen.getLongitud() + "," + origen.getLatitud() + ";"
                       + destino.getLongitud() + "," + destino.getLatitud();
               OsrmRouteDto resultados = distanciaClient.obtenerTiempoYDistancia(coordenadas);
               distanciaTotal += resultados.getDistance();
               DuracionTotal += resultados.getDuration();
           }
           RutaSugeridaDto rutaSugerida = new RutaSugeridaDto();
           rutaSugerida.setDistancia(distanciaTotal);
           rutaSugerida.setDuracion(DuracionTotal);
           rutaSugerida.setRuta(ruta);
           rutaSugerida.setCantidadTramos(ruta.getTramos().size());
           rutaSugeridas.add(rutaSugerida);
        };
        log.info("devolviendo rutas tentativas");
        return rutaSugeridas;
    }
}
