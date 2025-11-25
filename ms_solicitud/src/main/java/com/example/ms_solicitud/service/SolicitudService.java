package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.*;
import com.example.ms_solicitud.model.cambioEstado.CambioEstado;
import com.example.ms_solicitud.model.cambioEstado.CambioEstadoId;
import com.example.ms_solicitud.model.dto.*;
import com.example.ms_solicitud.repository.ClienteRepository;
import com.example.ms_solicitud.repository.ContenedorRepository;
import com.example.ms_solicitud.repository.SolicitudRepository;
import com.example.ms_solicitud.repository.TarifaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

        Cliente cliente;
        log.info("buscando cliente con ID:" + solicitudDto.getCliente().getDni());
        if (!clienteRepository.existsById(solicitudDto.getCliente().getDni())) {
            log.info("cliente no encontrado, creando uno nuevo");
            cliente = clienteRepository.save(solicitudDto.getCliente());
            log.info("Cliente creado");

        } else{
            cliente = clienteRepository.findById(solicitudDto.getCliente().getDni())
                    .orElseThrow(() -> new RuntimeException("cliente no encontrado"));
        }
        solicitud.setCliente(cliente);

        log.info("creando contenedor");
        Optional<Contenedor> contenedorEncontrado = contenedorRepository.findById(solicitudDto.getContenedor().getIdContenedor());
        Contenedor contenedor;
        if(contenedorEncontrado.isEmpty()) {
            log.info("Contenedor con ID: " + solicitudDto.getContenedor().getIdContenedor() + " no encontrado, creando uno nuevo...");
            contenedor = solicitudDto.getContenedor();
            contenedor.setIdContenedor(null);
            contenedor.setEstado("EN ORIGEN");
            contenedor = contenedorRepository.save(contenedor);
            log.info("Contenedor nuevo creado y marcado como EN ORIGEN.");
        } else{
            contenedor = contenedorEncontrado.get();
            if(contenedor.getEstado().equals("EN DESTINO")){
                log.info("Contenedor disponible, asignandolo a la solicitud y marcandolo como en origen");
                contenedor.setEstado("EN ORIGEN");
                contenedor.setTiempoEstadia(0);
                contenedor = contenedorRepository.save(contenedor);
            }
            else{
                log.error("No se puede asignar el contenedor a la solicitud, ya se encuentra en otra solicitud");
            }}
            log.info("Solicitud creada en estado: CREADA");
            solicitud.setContenedor(contenedor);
            solicitud.setCoordenadasOrigen(solicitudDto.getCoordenadasOrigen());
            solicitud.setCoordenadasDestino(solicitudDto.getCoordenadasDestino());
            solicitud.setEstadoActual(EstadoSolicitud.CREADA);

        log.info("creando solicitud");
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public Solicitud actualizarSolicitud(Integer numero, Solicitud solicitudActualizada) {

        Solicitud solicitudExistente = obtenerSolicitudPorNumero(numero);

        solicitudExistente.setNumero(solicitudActualizada.getNumero());
        solicitudExistente.setCostoEstimado(solicitudActualizada.getCostoEstimado());
        solicitudExistente.setTiempoEstimado(solicitudActualizada.getTiempoEstimado());
        solicitudExistente.setTiempoReal(solicitudActualizada.getTiempoReal());

        solicitudExistente.setCostoFinal(solicitudActualizada.getCostoFinal());
        solicitudExistente.setContenedor(solicitudActualizada.getContenedor());
        solicitudExistente.setCliente(solicitudActualizada.getCliente());
        solicitudExistente.setIdTarifa(solicitudActualizada.getIdTarifa());
        solicitudExistente.setCoordenadasOrigen(solicitudActualizada.getCoordenadasOrigen());
        solicitudExistente.setCoordenadasDestino(solicitudActualizada.getCoordenadasDestino());
        solicitudExistente.setEstadoActual(solicitudActualizada.getEstadoActual());
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
        solicitud.setEstadoActual(EstadoSolicitud.valueOf(estado));
        log.info("Actualizando estado de solicitud " + idSolicitud + " a " + estado);
        return solicitudRepository.save(solicitud);
    }
    @Transactional
    public Solicitud asignarRuta(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        List<RutaSugeridaDto> rutasSugeridas = rutasClient.buscarRutastentativas(idSolicitud, 2);
        RutaSugeridaDto rutaSugeridaSeleccionada = null;
        Double costoAproximado = 0.0;
        for(RutaSugeridaDto rutaSugerida : rutasSugeridas) {
            if (costoAproximado == 0.0) {
                costoAproximado = rutaSugerida.getCostoEstimado();
                rutaSugeridaSeleccionada = rutaSugerida;
            } else {
                if ((costoAproximado >= rutaSugerida.getCostoEstimado())) {
                    costoAproximado = rutaSugerida.getCostoEstimado();
                    rutaSugeridaSeleccionada = rutaSugerida;
                }
            }
        }
        if (rutaSugeridaSeleccionada == null) {
            throw new IllegalStateException("Error al seleccionar la mejor ruta tentativa.");
        }
        if (solicitud.getEstadoActual() == EstadoSolicitud.CREADA) {
            solicitud.setEstadoActual(EstadoSolicitud.APROBADA);

        }

        Integer idRuta = rutasClient.crearRuta(rutaSugeridaSeleccionada , solicitud.getCoordenadasOrigen() , solicitud.getCoordenadasDestino(), idSolicitud);
        solicitud.setCostoEstimado(rutaSugeridaSeleccionada.getCostoEstimado());
        solicitud.setTiempoEstimado(rutaSugeridaSeleccionada.getDuracion());
        solicitud.setIdRuta(idRuta);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public Solicitud asignarTarifa(Integer idSolicitud, Integer tarifaId){
        Tarifa tarifa = tarifaRepository.findById(tarifaId)
        .orElseThrow(() -> {
            log.warn("tarifa con ID: " + tarifaId + "no encontrada");
            throw new NoSuchElementException("Tarifa no encontrada con ID: " + tarifaId);
            });
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);;
        solicitud.setIdTarifa(tarifa);
        log.info("Tarifa " + tarifaId + " asignada a solicitud " + idSolicitud);
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

   @Transactional
   public DatosSolicitudDto obtenerDatosSolicitudPorNumero(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        DatosSolicitudDto datosSolicitud = new DatosSolicitudDto();
        datosSolicitud.setCoordenadasDestino(solicitud.getCoordenadasDestino());
        datosSolicitud.setCoordenadasOrigen(solicitud.getCoordenadasOrigen());
        datosSolicitud.setPesoContenedor(solicitud.getContenedor().getPeso());
        datosSolicitud.setVolumenContendor(solicitud.getContenedor().getVolumen());
        datosSolicitud.setValorVolumenTarifa(solicitud.getIdTarifa().getValorPorVolumen());
        datosSolicitud.setValorTramoTarifa(solicitud.getIdTarifa().getValorFijoTramo());

        return datosSolicitud;
   }
   @Transactional
    public List<HistorialDto> obtenerHistorialSolicitudPorNumero(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        List<HistorialDto> historialDtos = new ArrayList<>();
        for (CambioEstado cambioEstado : solicitud.getCambioEstado()) {
            HistorialDto historialDto = new HistorialDto();
            historialDto.setEstado(cambioEstado.getEstado());
            historialDto.setFechaInicio(cambioEstado.getCambioEstadoId().getFechaCambio());
            historialDtos.add(historialDto);
        }
        return historialDtos;
   }

    @Transactional
    public void iniciarSolicitud(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        LocalDateTime fecha = LocalDateTime.now();

        CambioEstado cambioEstado = new CambioEstado();

        CambioEstadoId cambioEstadoId = new CambioEstadoId();
        cambioEstadoId.setFechaCambio(fecha);
        cambioEstadoId.setIdSolicitud(idSolicitud);
        cambioEstado.setCambioEstadoId(cambioEstadoId);
        cambioEstado.setEstado(EstadoSolicitud.EN_CURSO);
        cambioEstado.setSolicitud(solicitud);

        if (solicitud.getCambioEstado() == null) {
            solicitud.setCambioEstado(new ArrayList<>());
        }

        solicitud.getCambioEstado().add(cambioEstado);
        solicitud.setEstadoActual(EstadoSolicitud.EN_CURSO);

        if (solicitud.getContenedor() != null) {
            solicitud.getContenedor().setEstado("EN CAMINO");
        }

        solicitudRepository.save(solicitud);
    }

    @Transactional
    public void reanudarViajeContenedor(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        solicitud.getContenedor().setEstado("EN CAMINO");
        Solicitud solicitudActualizada = solicitudRepository.save(solicitud);
    }
    @Transactional
    public void dejarContenedorEnDeposito(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        solicitud.getContenedor().setEstado("EN DEPOSITO");
        Solicitud solicitudActualizada = solicitudRepository.save(solicitud);
    }

    @Transactional
    public void finalizarSolicitud(Integer idSolicitud) {
        Solicitud solicitud = obtenerSolicitudPorNumero(idSolicitud);
        LocalDateTime fecha = LocalDateTime.now();
        LocalDateTime fechaFin = LocalDateTime.now();

        CambioEstado cambioEstado = new CambioEstado();
        CambioEstadoId cambioEstadoId = new CambioEstadoId();
        cambioEstadoId.setFechaCambio(fecha);
        cambioEstadoId.setIdSolicitud(idSolicitud);
        cambioEstado.setCambioEstadoId(cambioEstadoId);
        cambioEstado.setEstado(EstadoSolicitud.FINALIZADA);
        cambioEstado.setSolicitud(solicitud);

        if (solicitud.getCambioEstado() == null) {
            solicitud.setCambioEstado(new ArrayList<>());
        }

        solicitud.getCambioEstado().add(cambioEstado);
        solicitud.setEstadoActual(EstadoSolicitud.FINALIZADA);

        if (solicitud.getContenedor() != null) {
            solicitud.getContenedor().setEstado("ENTREGADO");
        }

        solicitud.getCambioEstado().stream()
                .filter(c -> c.getEstado() == EstadoSolicitud.EN_CURSO)
                .findFirst()
                .ifPresent(inicio -> {
                    LocalDateTime fechaInicio = inicio.getCambioEstadoId().getFechaCambio();
                    long segundos = Duration.between(fechaInicio, fechaFin).getSeconds();
                    solicitud.setTiempoReal((double) segundos);
                });

        try {
            CostoFinalDto costosReales = rutasClient.getCostos(solicitud.getIdRuta());

            Double costoCalculado = solicitud.getIdTarifa().calcularCostos(costosReales, solicitud.getContenedor().getVolumen());
            solicitud.setCostoFinal(costoCalculado);

            log.info("Costo final calculado y guardado: " + costoCalculado);
        } catch (Exception e) {
            log.error("Error al calcular el costo final: " + e.getMessage());
        }
        solicitudRepository.save(solicitud);
    }

}