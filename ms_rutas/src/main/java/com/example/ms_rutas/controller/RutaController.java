package com.example.ms_rutas.controller;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.dto.CostoFinalDto;
import com.example.ms_rutas.model.dto.RutaSugeridaDto;
import com.example.ms_rutas.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/rutas")
public class RutaController {
    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    public ResponseEntity<List<Ruta>> getRutas() {
        return ResponseEntity.ok(rutaService.obtenerTodosLasRutas());
    }
    //get
    @GetMapping("/{idRuta}")
    public ResponseEntity<Ruta> getRutaPorId(@PathVariable Integer idRuta) {
        return ResponseEntity.ok(rutaService.obtenerRutaPorId(idRuta));
    }
    @GetMapping("/costos/{idRuta}")
    public CostoFinalDto getCostos(@PathVariable String idRuta) {
        Integer numeroRuta = Integer.parseInt(idRuta);
        return rutaService.obtenerCostos(numeroRuta);
    }
    @GetMapping("/rutas-tentativas")
    public List<RutaSugeridaDto> getRutasTentativas(@RequestParam Integer idSolicitud, @RequestParam Integer cantidadDepositosMax) {
        return rutaService.consultarRutasTentativas(idSolicitud, cantidadDepositosMax);

    }
    //put
    @PutMapping({"/{idRuta}"})
    public ResponseEntity<Ruta> actualizarRuta(@PathVariable Integer idRuta, @RequestBody Ruta ruta) {
        return ResponseEntity.ok(rutaService.actualizarRuta(idRuta, ruta));
    }

    //post
    @PostMapping
    public ResponseEntity<Integer> crearRuta(@RequestBody RutaSugeridaDto rutaSugeridaDto, @RequestParam String coordenadasOrigen , @RequestParam String coordenadasDestino, @RequestParam Integer idSolicitud) {
        Integer idRutaCreada = rutaService.crearRuta(rutaSugeridaDto, coordenadasOrigen, coordenadasDestino, idSolicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(idRutaCreada);
    }
    @PutMapping("/asignacion-tramos/{idRuta}")
    public ResponseEntity<Ruta> asignarTramosARuta(@PathVariable Integer idRuta, @RequestBody List<Tramo> tramos) {
        return ResponseEntity.ok(rutaService.asignarTramosARuta(idRuta, tramos));
    }

    //delete
    @DeleteMapping({"/{idRuta}"})
    public ResponseEntity<Void> eliminarRuta(@PathVariable Integer idRuta) {
        rutaService.eliminarRuta(idRuta);
        return ResponseEntity.noContent().build();
    }


}
