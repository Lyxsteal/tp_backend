package com.example.ms_rutas.controller;

import com.example.ms_rutas.model.Ciudad;
import com.example.ms_rutas.model.Deposito;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.Ubicacion;
import com.example.ms_rutas.service.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rutas/ubicaciones")
public class UbicacionController {
    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public ResponseEntity<List<Ubicacion>> getUbicaciones() {
        return ResponseEntity.ok(ubicacionService.obtenerTodasLasUbicaciones());
    }
    //get
    @GetMapping ("/{idUbicacion}")
    public ResponseEntity<Ubicacion>  obtenerUbicacion(@PathVariable Integer idUbicacion) {
        return ResponseEntity.ok( ubicacionService.obtenerUbicacionPorId(idUbicacion));
    }

    //post
    @PostMapping
    public ResponseEntity<Ubicacion> crearUbicacion(@RequestBody Ubicacion ubicacion) {
        return ResponseEntity.ok(ubicacionService.crearUbicacion(ubicacion));
    }
    //put
    @PutMapping("/{idUbicacion}")
    public ResponseEntity<Ubicacion> actualizarUbicacion(@PathVariable Integer idUbicacion , @RequestBody Ubicacion ubicacionActualizada) {
        return ResponseEntity.ok(ubicacionService.actualizarUbicacion(idUbicacion,ubicacionActualizada ));
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUbicacion(@PathVariable Integer id) {
        ubicacionService.eliminarUbicacion(id);
        return ResponseEntity.noContent().build();
    }
}
