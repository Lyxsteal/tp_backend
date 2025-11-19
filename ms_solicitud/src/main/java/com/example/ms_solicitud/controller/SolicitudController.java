package com.example.ms_solicitud.controller;

import com.example.ms_solicitud.model.Solicitud;
import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.security.AuthenticatedUser;
import com.example.ms_solicitud.security.UserHeaderInterceptor;
import com.example.ms_solicitud.service.ContenedorService;
import com.example.ms_solicitud.service.SolicitudService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {
    private final SolicitudService service;

    @GetMapping
    public ResponseEntity<List<Solicitud>> obtenerSolicitudes(HttpServletRequest request) {

        AuthenticatedUser user = (AuthenticatedUser) request.getAttribute(UserHeaderInterceptor.USER_ATTR);

        System.out.println("USER => " + user);

        List<Solicitud> lista = service.obtenerTodosLasSolicitudes();

        return ResponseEntity.ok(lista);
    }
    //get
    @GetMapping("/{idSolicitud}")
    public ResponseEntity<Solicitud> obtenerSolicitud(@PathVariable Integer idSolicitud){
        Solicitud solicitud = service.obtenerSolicitudPorNumero(idSolicitud);
        return ResponseEntity.ok(solicitud);

    }

    @GetMapping("solicitud-por-cliente/{idCliente}")
    public List<Solicitud> obtenerSolicitudesPorClientes(@PathVariable Integer idCliente) {
        return service.obtenerSolicitudesPorCliente(idCliente);
    }

   @GetMapping("costo-final/{idSolicitud}")
    public Double calcularCostoFinal(@PathVariable Integer idSolicitud) {
        return service.calcularCostoFinal(idSolicitud);
    }
    //put
    @PutMapping("estados/{idSolicitud}")
    public ResponseEntity<Solicitud> actualizarEstado(@PathVariable Integer idSolicitud, @RequestBody String estado){
    return ResponseEntity.ok(service.actualizarEstado(idSolicitud, estado));
    }

    @PutMapping("asignar-ruta/{idSolicitud}")
    public ResponseEntity<Solicitud> asignarRuta(@PathVariable Integer idSolicitud, @RequestBody Integer idRuta){
        return ResponseEntity.ok(service.asignarRuta(idSolicitud, idRuta));
    }
    @PutMapping("asignar-tarifa/{idSolicitud}")
    public ResponseEntity<Solicitud> asignarTarifa(@PathVariable Integer idSolicitud, @RequestBody Tarifa tarifa){
        return ResponseEntity.ok(service.asignarTarifa(idSolicitud, tarifa));
    }
    //post
    @PostMapping
    public ResponseEntity<Solicitud> crearSolicitud (@RequestBody Solicitud solicitud){
        service.crearSolicitud(solicitud);
        return ResponseEntity.ok(solicitud);
    }

    //put
}