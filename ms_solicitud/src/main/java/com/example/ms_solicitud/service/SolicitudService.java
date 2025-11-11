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
    public SolicitudService( SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
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

//    @Transactional
//    public Solicitud calcularCostoFinal(Solicitud solicitud) {
//
//
//    }

}