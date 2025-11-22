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
            tramo.setCoordenadasOrigen(tramoDto.getCoordenadasOrigen());
            tramo.setCoordenadasDestino(tramoDto.getCoordenadasDestino());
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

        if (tramos == null || tramos.isEmpty()) {
            return 0.0;
        }

        for (Tramo tramo : tramos) {

            if (tramo.getCamion() == null) {
                log.warn("El tramo con orden {} no tiene camión asignado. Se omite del cálculo de consumo.", tramo.getNroOrden());
                continue;
            }

            if (tramo.getCoordenadasOrigen() == null || tramo.getCoordenadasDestino() == null) {
                log.warn("El tramo con orden {} tiene coordenadas incompletas. Se omite.", tramo.getNroOrden());
                continue;
            }

            Double distancia = calcularDistancia(tramo);

            if (distancia > 0) {
                Double consumoCombustible = distancia * tramo.getCamion().getConsCombKm() / 1000;
                consumoTotal += consumoCombustible;
            }
        }

        log.info("El consumo total calculado es: " + consumoTotal);
        return consumoTotal;
    }

    public Double calcularDistancia(Tramo tramo){
        String origen = tramo.getCoordenadasOrigen();
        String destino = tramo.getCoordenadasDestino();

        if (origen == null || destino == null || origen.trim().isEmpty() || destino.trim().isEmpty()) {
            log.error("No se puede calcular distancia: Coordenadas nulas en tramo ID: " + tramo.getIdTramo());
            return 0.0;
        }

        String coordenadas = origen + ";" + destino;

        try {
            Double distancia = distanciaClient.obtenerDistancia(coordenadas);
            log.info("Distancia calculada: " + distancia);
            return distancia;
        } catch (Exception e) {
            log.error("Error al llamar a API de distancias para coordenadas: " + coordenadas, e);
            return 0.0;
        }
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
        String[] partesOrigen = datosSolicitud.getCoordenadasOrigen().replace(" ","").trim().split(",");
        String latitudOr = partesOrigen[0];
        String longitudOr = partesOrigen[1];
        String[] partesDestino = datosSolicitud.getCoordenadasDestino().replace(" ","").trim().split(",");
        String latitudDes = partesDestino[0].trim();
        String longitudDes = partesDestino[1].trim();
        String coordenadas = longitudOr + "," + latitudOr + ";" + longitudDes + "," + latitudDes;
        OsrmRouteDto tiempoYDistancia = distanciaClient.obtenerTiempoYDistancia(coordenadas);
        RutaSugeridaDto rutaSugeridaDto = new RutaSugeridaDto();
        rutaSugeridaDto.setNumeroDeAlternativa(1);
        TramoSugeridoDto tramoSugeridoDto = new TramoSugeridoDto();
        tramoSugeridoDto.setCoordenadasOrigen(datosSolicitud.getCoordenadasOrigen());
        tramoSugeridoDto.setCoordenadasDestino((datosSolicitud.getCoordenadasDestino()));
        tramoSugeridoDto.setNroOrden(1);
        rutaSugeridaDto.setDuracion(tiempoYDistancia.getDuration());
        rutaSugeridaDto.setDistancia(tiempoYDistancia.getDistance());
        rutaSugeridaDto.getTramos().add(tramoSugeridoDto);
        Double consPromCamiones = camionRepository.promedioConsumo(datosSolicitud.getVolumenContendor(), datosSolicitud.getPesoContenedor());
        Double costo = datosSolicitud.getValorVolumenTarifa()* datosSolicitud.getVolumenContendor() + datosSolicitud.getValorTramoTarifa()*rutaSugeridaDto.getTramos().size() + consPromCamiones * rutaSugeridaDto.getDistancia()/1000;
        rutaSugeridaDto.setCostoEstimado(costo);
        return  rutaSugeridaDto;
    }
    public RutaSugeridaDto rutaSugeridaConDeposito(DatosSolicitudDto datosSolicitud) {
        List<Deposito> depositos = depositoRepository.findAll();
        String coordenadasMenor = "";
        Double distanciamenor = 0.0;
        String[] partesOrigen = datosSolicitud.getCoordenadasOrigen().replace(" ","").trim().split(",");
        String latitudOr = partesOrigen[0];
        String longitudOr = partesOrigen[1];
        String[] partesDestino = datosSolicitud.getCoordenadasDestino().replace(" ","").trim().split(",");
        String latitudDes = partesDestino[0];
        String longitudDes = partesDestino[1];
        Double distanciaOrDes = distanciaClient.obtenerDistancia(longitudOr + "," + latitudOr + ";" + longitudDes + "," + latitudDes);
        for (Deposito deposito : depositos) {
            String[] partesDeposito = deposito.getCoordenadas().replace(" ","").trim().split(",");
            String latitudDep = partesDeposito[0];
            String longitudDep = partesDeposito[1];
            String coordenadasOrDep = longitudOr + "," + latitudOr + ";" + longitudDep + "," + latitudDep;
            String coordenadasDepDes = longitudDep + "," + latitudDep + ";" + longitudDes + "," + latitudDes;
            Double distanciaOrDep =  distanciaClient.obtenerDistancia(coordenadasOrDep);
            Double distanciaDepDes = distanciaClient.obtenerDistancia(coordenadasDepDes);
            if (coordenadasMenor.isEmpty()){
                coordenadasMenor = deposito.getCoordenadas();
                distanciamenor = distanciaOrDep;
            }
            else {
                if ((distanciaOrDep <= distanciamenor) && (distanciaDepDes <= distanciaOrDes)){
                    coordenadasMenor = deposito.getCoordenadas();
                    distanciamenor = distanciaOrDes;
                }
            }
        }
        String[] coordDepElegido = coordenadasMenor.replace(" ","").trim().split(",");
        String latitudDepElec = coordDepElegido[0];
        String longitudDepElec = coordDepElegido[1];
        OsrmRouteDto tiempoYDistancia1 = distanciaClient.obtenerTiempoYDistancia(longitudOr + "," + latitudOr + ";" + longitudDepElec + "," + latitudDepElec);
        OsrmRouteDto tiempoYDistancia2 = distanciaClient.obtenerTiempoYDistancia(longitudDepElec + "," + latitudDepElec + ";" + longitudDes + "," + latitudDes );
        RutaSugeridaDto rutaSugeridaDto = new RutaSugeridaDto();
        rutaSugeridaDto.setNumeroDeAlternativa(2);
        TramoSugeridoDto tramoSugeridoDto1 = new TramoSugeridoDto();
        tramoSugeridoDto1.setCoordenadasOrigen(datosSolicitud.getCoordenadasOrigen());
        tramoSugeridoDto1.setCoordenadasDestino(coordenadasMenor);
        tramoSugeridoDto1.setNroOrden(1);
        rutaSugeridaDto.getTramos().add(tramoSugeridoDto1);
        TramoSugeridoDto tramoSugeridoDto2 = new TramoSugeridoDto();
        tramoSugeridoDto2.setCoordenadasOrigen(coordenadasMenor);
        tramoSugeridoDto2.setCoordenadasDestino(datosSolicitud.getCoordenadasDestino());
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
