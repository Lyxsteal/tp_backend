package com.example.ms_rutas.controller;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.dto.CostoFinalDto;
import com.example.ms_rutas.service.RutaService;
import lombok.RequiredArgsConstructor;
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
    //put
    @PutMapping({"/{idRuta}"})
    public ResponseEntity<Ruta> actualizarRuta(@PathVariable Integer idRuta, @RequestBody Ruta ruta) {
        return ResponseEntity.ok(rutaService.actualizarRuta(idRuta, ruta));
    }

    //put
    //post
    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@RequestBody Ruta ruta) {
        return ResponseEntity.ok(rutaService.crearRuta(ruta));
    }
    @PutMapping("/asignacion-tramos/{idRuta}")
    public ResponseEntity<Ruta> asignarTramosARuta(@PathVariable Integer idRuta, @RequestBody List<Tramo> tramos) {
        return ResponseEntity.ok(rutaService.asignarTramosARuta(idRuta, tramos));
    }

    //delete
    @DeleteMapping({"/id"})
    public ResponseEntity<Void> eliminarRuta(@PathVariable Integer id) {
        rutaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }
}
