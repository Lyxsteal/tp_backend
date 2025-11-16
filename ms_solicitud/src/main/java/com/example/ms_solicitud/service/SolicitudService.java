package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Solicitud;
import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.repository.SolicitudRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final RutasClient rutasClient;
    public SolicitudService( SolicitudRepository solicitudRepository , RutasClient rutasClient ) {
        this.solicitudRepository = solicitudRepository;
        this.rutasClient = rutasClient;
    }
    @Transactional
    public List<Solicitud> obtenerTodosLasSolicitudes() {
        return solicitudRepository.findAll();
    }

    @Transactional
    public Solicitud obtenerSolicitudPorNumero(Integer numero) {
        return solicitudRepository.findById(numero)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con numero: " + numero));
    }

    @Transactional
    public Solicitud crearSolicitud(Solicitud solicitud) {
        if (solicitudRepository.existsById(solicitud.getNumero())) {
            throw new RuntimeException("Ya existe una solicitud con el numero: " + solicitud.getNumero());
        }
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
        solicitudExistente.setNumeroContenedor(solicitudActualizada.getNumeroContenedor());
        solicitudExistente.setDniCliente(solicitudActualizada.getDniCliente());
        solicitudExistente.setIdTarifa(solicitudActualizada.getIdTarifa());
        solicitudExistente.setCoordenadasOrigen(solicitudActualizada.getCoordenadasOrigen());
        solicitudExistente.setCoordenadasDestino(solicitudActualizada.getCoordenadasDestino());

        return solicitudRepository.save(solicitudExistente);
    }

    @Transactional
    public void eliminarSolicitud(Integer id) {
        if (!solicitudRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Solicitud no encontrada con ID: " + id);
        }
        solicitudRepository.deleteById(id);
    }
    @Transactional
    public List<Solicitud> obtenerSolicitudesPorCliente(Integer idCliente) {
        if (!solicitudRepository.existsById(idCliente)) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + idCliente);
        }
        return solicitudRepository.findAllByCliente(idCliente);
    }
    @Transactional
    public Solicitud actualizarEstado(Integer idSolicitud, Solicitud solicitudActualizada) {
        return solicitudRepository.save(solicitudActualizada);
    }
    @Transactional
    public Solicitud asignarRuta(Integer isSolicitud, Integer idRuta){
        Solicitud solicitud = obtenerSolicitudPorNumero(idRuta);
        boolean rutaExiste = rutasClient.verificarRuta(idRuta);
        if(!rutaExiste) {
            throw new RuntimeException("Ruta no existe");
        }

        solicitud.setIdRuta(idRuta);
        return solicitudRepository.save(solicitud);
    }

   @Transactional
   public Float calcularCostoFinal(Integer idSolicitud) {
        Integer idRuta = solicitudRepository.findById(idSolicitud).get().getIdRuta();
<<<<<<< HEAD
        Float costoFinalCalculado = 0.0f;
        return costoFinalCalculado;
=======

>>>>>>> f581931c1e0854a69fad4d91b4014d970ada910f

   }

}