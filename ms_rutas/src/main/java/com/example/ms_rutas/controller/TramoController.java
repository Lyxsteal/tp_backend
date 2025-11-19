package com.example.ms_rutas.controller;

import com.example.ms_rutas.model.TipoTramo;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.service.TramoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rutas/tramos")
@RequiredArgsConstructor
public class TramoController {
    private final TramoService tramoService;

    @GetMapping
    public ResponseEntity<List<Tramo>> getTramos() {
        return ResponseEntity.ok(tramoService.obtenerTodasLosTramos());
    }

    @GetMapping("/{idTipoTramo}")
    public ResponseEntity<Tramo> obtenerPorId(@PathVariable Integer idTipoTramo) {
        return ResponseEntity.ok(tramoService.obtenerTramoPorId(idTipoTramo));
    }

    @PutMapping("/camion/{idTramo}")
    public ResponseEntity<Tramo> AsignarCamionATramo(@PathVariable Integer idTramo, @RequestBody String camion_patente ) {
        return ResponseEntity.ok(tramoService.asignarCamionATramo(idTramo,camion_patente));

    }
    @GetMapping("/tramos-asignados/{cedula}")
    public List<Tramo> obtenerTramosAsignadosCamionero(@PathVariable Integer cedula){
        return tramoService.obtenerTramosPorCamionero(cedula);
    }


    //put
    @PutMapping("/estado/{idTramo}")
    public ResponseEntity<Tramo> ActualizarEstadoTramo(@PathVariable Integer idTramo,@RequestBody String estadotramo) {
        return ResponseEntity.ok(tramoService.actualizarEstadoTramo(idTramo,estadotramo));
    }

    //put
    @PutMapping("/{idTramo}")
    public ResponseEntity<Tramo> actualizarTramo(@PathVariable Integer id, @RequestBody Tramo tramo) {
        return ResponseEntity.ok(tramoService.actualizarTramo(id, tramo));
    }

    //post
    @PostMapping
    public ResponseEntity<Tramo> crearTramo(@RequestBody Tramo tramo) {
        return ResponseEntity.ok(tramoService.crearTramo(tramo));
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTramo(@PathVariable Integer id) {
        tramoService.eliminarTramo(id);
        return ResponseEntity.noContent().build();
    }
}
