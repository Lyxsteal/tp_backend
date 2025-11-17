package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Cliente;
import com.example.ms_solicitud.model.Contenedor;
import com.example.ms_solicitud.model.Solicitud;
import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.model.dto.CostoFinalDto;
import com.example.ms_solicitud.repository.ClienteRepository;
import com.example.ms_solicitud.repository.ContenedorRepository;
import com.example.ms_solicitud.repository.SolicitudRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final RutasClient rutasClient;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;

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
        //ver si existe el cliente
        if (!clienteRepository.existsById(solicitud.getDniCliente().getDni())) {
            Cliente cliente = clienteRepository.save(solicitud.getDniCliente());
        }
        //crea el contenedor
        Contenedor contenedor = contenedorRepository.save(solicitud.getNumeroContenedor());

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
    public Solicitud asignarRuta(Integer idSolicitud, Integer idRuta){
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        boolean rutaExiste = rutasClient.verificarRuta(idRuta);
        if(!rutaExiste) {
            throw new RuntimeException("Ruta no existe");
        }

        solicitud.setIdRuta(idRuta);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public Solicitud asignarTarifa(Integer idSolicitud, Tarifa tarifa){
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);

        solicitud.setIdTarifa(tarifa);
        return solicitudRepository.save(solicitud);
    }

   @Transactional
   public Double calcularCostoFinal(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        Integer idRuta = solicitud.getIdRuta();
        CostoFinalDto costo = rutasClient.getCostos(idRuta);

        Double costoFinalCalculado = solicitud.getIdTarifa().calcularCostos(costo,solicitud.getNumeroContenedor().getVolumen()) ;
        return costoFinalCalculado;


   }

}