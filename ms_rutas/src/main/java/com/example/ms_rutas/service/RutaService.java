package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Deposito;
import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.TipoTramo;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.dto.*;
import com.example.ms_rutas.repository.CamionRepository;
import com.example.ms_rutas.repository.DepositoRepository;
import com.example.ms_rutas.repository.RutaRepository;
import com.example.ms_rutas.repository.TramoRepository;
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
    private final  SolicitudClient solicitudClient;
    private final DepositoRepository depositoRepository;
    private final CamionRepository camionRepository;

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
                    log.warn("La ruta no existe con id: "+ id);
                    return new RuntimeException("Ruta no encontrado con id: " + id);
                });
    }

    @Transactional
    public Integer crearRuta(RutaSugeridaDto rutaSugeridaDto,String coordenadasOrigen , String coordenadasDestino, Integer idSolicitud) {
        Ruta ruta = new Ruta();


        List<Tramo> tramosEntidad = new ArrayList<>();

        for (TramoSugeridoDto tramoDto : rutaSugeridaDto.getTramos()) {
            Tramo tramo = new Tramo();
            tramo.setNroOrden(tramoDto.getNroOrden());
            tramo.setCoordenadasOrigen(tramoDto.getCordenadasOrigen());
            tramo.setCoordenadasDestino(tramoDto.getCordenadasDestino());
            tramo.setEstadoTramo("PENDIENTE");
            String tipotramoStr = "";
            if (tramo.getCoordenadasOrigen().equals(coordenadasOrigen)){
                tipotramoStr = "ORIGEN";
            }
            else {
                tipotramoStr = "DEPOSITO";
            }
            if (tramo.getCoordenadasDestino().equals(coordenadasDestino)){
                tipotramoStr = tipotramoStr + "_DESTINO";
            }
            else {
                tipotramoStr = tipotramoStr + "_DEPOSITO";
            }
            tramo.setTipoTramo(TipoTramo.valueOf(tipotramoStr));
            tramo.setRuta(ruta);
            tramosEntidad.add(tramo);
        }
        ruta.setIdSolicitud(idSolicitud);
        ruta.setTramos(tramosEntidad);
        ruta = rutaRepository.save(ruta);

        return ruta.getIdRuta();
    }

    @Transactional
    public Ruta actualizarRuta(Integer id, Ruta rutaActualizada) {
        Ruta rutaExistente = obtenerRutaPorId(id);
        rutaExistente.setTramos(rutaActualizada.getTramos());
        log.info("Ruta actualizada");
        return rutaRepository.save(rutaExistente);
    }

    @Transactional
    public void eliminarRuta(Integer id) {
        if (!rutaRepository.existsById(id)) {
            log.warn("No se puedo eliminar. La ruta no se encontro con id: "+id);
            throw new RuntimeException("No se puede eliminar. Ruta no encontrada con id: " + id);
        }
        log.info("Ruta eliminada");
        rutaRepository.deleteById(id);
    }

    @Transactional
    public Ruta asignarTramosARuta(Integer idRuta, List<Tramo> tramos) {
        Ruta ruta = obtenerRutaPorId(idRuta);
        for(Tramo tramo: tramos){
            tramo.setRuta(ruta);
        }
        ruta.setTramos(tramos);
        return rutaRepository.save(ruta);
    }

    @Transactional

    public CostoFinalDto obtenerCostos(Integer idruta) {
        Ruta ruta = obtenerRutaPorId(idruta);
        log.info("Calculando el consumo de combustible");
        Double consumo = obtenerConsumoTotal(ruta.getTramos());
        log.info("Calculando la cantidad de dias");
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
        String origen = tramo.getCoordenadasOrigen();
        String destino = tramo.getCoordenadasDestino();

        String coordenadas = origen + ";" + destino;

        Double distancia = distanciaClient.obtenerDistancia(coordenadas);

        log.info("Distancia calculada");
        return distancia;

    }

    public List<RutaSugeridaDto> consultarRutasTentativas(Integer idSolicitud){
        List<RutaSugeridaDto> rutaSugeridas = new ArrayList<>();
        if (solicitudClient.verificarSolicitud(idSolicitud)) {
            log.info("Solicitud encontrada");
            DatosSolicitudDto datosSolicitudDto = solicitudClient.obtenerDatosSolicitudPorNumero(idSolicitud);
            rutaSugeridas.add(rutaSugeridaDirecta(datosSolicitudDto));
            rutaSugeridas.add(rutaSugeridaConDeposito(datosSolicitudDto));

        }
        else {
            log.warn("No existe la solicitud");
            throw  new RuntimeException("No existe la solicitud");
        }
        log.info("Devolviendo las rutas tentativas");
        return rutaSugeridas;
    }
    public RutaSugeridaDto rutaSugeridaDirecta (DatosSolicitudDto datosSolicitud) {
        String coordenadas = datosSolicitud.getCoordenadasOrigen() + ";" + datosSolicitud.getCoordenadasDestino();
        OsrmRouteDto tiempoYDistancia = distanciaClient.obtenerTiempoYDistancia(coordenadas);
        RutaSugeridaDto rutaSugeridaDto = new RutaSugeridaDto();
        rutaSugeridaDto.setNumeroDeAlternativa(1);
        TramoSugeridoDto tramoSugeridoDto = new TramoSugeridoDto();
        tramoSugeridoDto.setCordenadasOrigen(datosSolicitud.getCoordenadasOrigen());
        tramoSugeridoDto.setCordenadasDestino((datosSolicitud.getCoordenadasDestino()));
        tramoSugeridoDto.setNroOrden(1);
        rutaSugeridaDto.getTramos().add(tramoSugeridoDto);
        Double consPromCamiones = camionRepository.promedioConsumo(datosSolicitud.getVolumenContendor(), datosSolicitud.getPesoContenedor());
        Double costo = datosSolicitud.getValorVolumenTarifa()* datosSolicitud.getVolumenContendor() + datosSolicitud.getValorTramoTarifa()*rutaSugeridaDto.getTramos().size() + consPromCamiones * rutaSugeridaDto.getDistancia()/1000;
        return  rutaSugeridaDto;
    }
    public RutaSugeridaDto rutaSugeridaConDeposito(DatosSolicitudDto datosSolicitud) {
        List<Deposito> depositos = depositoRepository.findAll();
        String coordenadasMenor = "";
        Double distanciamenor = 0.0;
        Double distanciaOrDes = distanciaClient.obtenerDistancia(datosSolicitud.getCoordenadasOrigen() + ";" + datosSolicitud.getCoordenadasDestino());
        for (Deposito deposito : depositos) {
            String coordenadasDeposito = deposito.getCoordenadas();
            String coordenadasOrDep = datosSolicitud.getCoordenadasOrigen() + ";" + coordenadasDeposito;
            String coordenadasDepDes = coordenadasDeposito + ";" +datosSolicitud.getCoordenadasDestino();
            Double distanciaOrDep =  distanciaClient.obtenerDistancia(coordenadasOrDep);
            Double distanciaDepDes = distanciaClient.obtenerDistancia(coordenadasDepDes);
            if (coordenadasMenor.isEmpty()){
                coordenadasMenor = coordenadasDeposito;
                distanciamenor = distanciaOrDep;
            }
            else {
                if ((distanciaOrDep <= distanciamenor) && (distanciaDepDes <= distanciaOrDes)){
                    coordenadasMenor = coordenadasDeposito;
                    distanciamenor = distanciaOrDes;
                }
            }
        }
        OsrmRouteDto tiempoYDistancia1 = distanciaClient.obtenerTiempoYDistancia(datosSolicitud.getCoordenadasOrigen() + ";" + coordenadasMenor);
        OsrmRouteDto tiempoYDistancia2 = distanciaClient.obtenerTiempoYDistancia(coordenadasMenor + ";" + datosSolicitud.getCoordenadasDestino());
        RutaSugeridaDto rutaSugeridaDto = new RutaSugeridaDto();
        rutaSugeridaDto.setNumeroDeAlternativa(2);
        TramoSugeridoDto tramoSugeridoDto1 = new TramoSugeridoDto();
        tramoSugeridoDto1.setCordenadasOrigen(datosSolicitud.getCoordenadasOrigen());
        tramoSugeridoDto1.setCordenadasDestino(coordenadasMenor);
        tramoSugeridoDto1.setNroOrden(1);
        rutaSugeridaDto.getTramos().add(tramoSugeridoDto1);
        TramoSugeridoDto tramoSugeridoDto2 = new TramoSugeridoDto();
        tramoSugeridoDto2.setCordenadasOrigen(coordenadasMenor);
        tramoSugeridoDto2.setCordenadasDestino(datosSolicitud.getCoordenadasDestino());
        tramoSugeridoDto2.setNroOrden(2);
        rutaSugeridaDto.getTramos().add(tramoSugeridoDto2);
        rutaSugeridaDto.setDuracion(tiempoYDistancia1.getDuration() + tiempoYDistancia2.getDuration());
        rutaSugeridaDto.setDistancia(tiempoYDistancia1.getDistance() + tiempoYDistancia2.getDistance());
        Double consPromCamiones = camionRepository.promedioConsumo(datosSolicitud.getVolumenContendor(), datosSolicitud.getPesoContenedor());
        Double costo = datosSolicitud.getValorVolumenTarifa()* datosSolicitud.getVolumenContendor() + datosSolicitud.getValorTramoTarifa()*rutaSugeridaDto.getTramos().size() + consPromCamiones * rutaSugeridaDto.getDistancia()/1000;
        rutaSugeridaDto.setCostoEstimado(costo);
        return rutaSugeridaDto;

    }

}
