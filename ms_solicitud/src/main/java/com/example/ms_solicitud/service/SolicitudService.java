package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Cliente;
import com.example.ms_solicitud.model.Contenedor;
import com.example.ms_solicitud.model.Solicitud;
import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.model.dto.CostoFinalDto;
import com.example.ms_solicitud.model.dto.SolicitudDto;
import com.example.ms_solicitud.repository.ClienteRepository;
import com.example.ms_solicitud.repository.ContenedorRepository;
import com.example.ms_solicitud.repository.SolicitudRepository;
import com.example.ms_solicitud.repository.TarifaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final RutasClient rutasClient;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final TarifaRepository tarifaRepository;
    private static final Logger log = LoggerFactory.getLogger(SolicitudService.class);

    @Transactional
    public List<Solicitud> obtenerTodosLasSolicitudes() {
        return solicitudRepository.findAll();
    }

    @Transactional
    public Solicitud obtenerSolicitudPorNumero(Integer numero) {
        return solicitudRepository.findById(numero)
                .orElseThrow(() -> {
                    log.warn("Solicitud no encontrada con numero: " + numero);
                    return new RuntimeException("Solicitud no encontrada");
                    });
    }

    @Transactional
    public Solicitud crearSolicitud(SolicitudDto solicitudDto) {
        Solicitud solicitud = new Solicitud();
        Cliente cliente = solicitudDto.getCliente();
        //ver si existe el cliente
        log.info("buscando cliente con ID:" + solicitudDto.getCliente().getDni());
        if (!clienteRepository.existsById(solicitudDto.getCliente().getDni())) {
            log.info("cliente no encontrado, creando uno nuevo");
            cliente = clienteRepository.save(solicitudDto.getCliente());

            log.info("Cliente creado");
        }
        solicitud.setCliente(cliente);
        //crea el contenedor
        log.info("creando contenedor");
        Contenedor contenedor = contenedorRepository.save(solicitudDto.getContenedor());
        solicitud.setContenedor(contenedor);
        solicitud.setCoordenadasOrigen(solicitudDto.getCoordenadasOrigen());
        solicitud.setCoordenadasDestino(solicitudDto.getCoordenadasDestino());
        log.info("creando solicitud");
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public Solicitud actualizarSolicitud(Integer numero, Solicitud solicitudActualizada) {

        Solicitud solicitudExistente = obtenerSolicitudPorNumero(numero);

        solicitudExistente.setNumero(solicitudActualizada.getNumero());
        solicitudExistente.setCostoEstimado(solicitudActualizada.getConsumoEstimado());
        solicitudExistente.setTiempoEstimado(solicitudActualizada.getTiempoEstimado());
        solicitudExistente.setTiempoReal(solicitudActualizada.getTiempoReal());
        solicitudExistente.setConsumoEstimado(solicitudActualizada.getConsumoEstimado());
        solicitudExistente.setCostoFinal(solicitudActualizada.getCostoFinal());
        solicitudExistente.setContenedor(solicitudActualizada.getContenedor());
        solicitudExistente.setCliente(solicitudActualizada.getCliente());
        solicitudExistente.setIdTarifa(solicitudActualizada.getIdTarifa());
        solicitudExistente.setCoordenadasOrigen(solicitudActualizada.getCoordenadasOrigen());
        solicitudExistente.setCoordenadasDestino(solicitudActualizada.getCoordenadasDestino());
        log.info("Actualizando la solicitud" + numero);
        return solicitudRepository.save(solicitudExistente);
    }

    @Transactional
    public void eliminarSolicitud(Integer id) {
        if (!solicitudRepository.existsById(id)) {
            log.warn("No se pudo encontrar la solicitud con id:" + id);
            throw new RuntimeException("No se puede eliminar. Solicitud no encontrada con ID: " + id);
        }
        log.info("Eliminando la solicitud con id:" + id);
        solicitudRepository.deleteById(id);
    }
    @Transactional
    public List<Solicitud> obtenerSolicitudesPorCliente(Integer idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            log.warn("No se pudo encontrar cliente con dni:" + idCliente);
            throw new RuntimeException("Solicitud no encontrada con ID: " + idCliente);
        }
        return solicitudRepository.findAllByCliente(idCliente);
    }
    @Transactional
    public Solicitud actualizarEstado(Integer idSolicitud, String estado) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> {
                    log.warn("solicitud con ID: "+ idSolicitud + "no encontrada");
                    return new RuntimeException("no se encontro la solicitud");
                });
        solicitud.getContenedor().setEstado(estado);
        log.info("Actualizando estado de solicitud " + idSolicitud + " a " + estado);
        return solicitudRepository.save(solicitud);
    }
    @Transactional
    public Solicitud asignarRuta(Integer idSolicitud, Integer idRuta){
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        boolean rutaExiste = rutasClient.verificarRuta(idRuta);
        if(!rutaExiste) {
            log.warn("La ruta " + idRuta + " no existe.");
            throw new RuntimeException("Ruta no existe");
        }

        solicitud.setIdRuta(idRuta);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public Solicitud asignarTarifa(Integer idSolicitud, Tarifa tarifa){
        if(!tarifaRepository.existsById(tarifa.getIdTarifa())){
            log.warn("tarifa con ID: " + tarifa.getIdTarifa() + "no encontrada");
        }
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);;
        solicitud.setIdTarifa(tarifa);
        log.info("Tarifa " + tarifa.getIdTarifa() + " asignada a solicitud " + idSolicitud);
        return solicitudRepository.save(solicitud);
    }

   @Transactional
   public Double calcularCostoFinal(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);

        Integer idRuta = solicitud.getIdRuta();
        log.info("obteniendo los costos");
        CostoFinalDto costo = rutasClient.getCostos(idRuta);

        Double costoFinalCalculado = solicitud.getIdTarifa().calcularCostos(costo,solicitud.getContenedor().getVolumen()) ;
        log.info("costo final calculado");

        return costoFinalCalculado;
   }

}