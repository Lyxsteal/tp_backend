package com.example.ms_rutas.controller;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.TipoTramo;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.service.CamionService;
import com.example.ms_rutas.service.TipoTramoService;
import com.example.ms_rutas.service.TramoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rutas/tipo-tramos")
@RequiredArgsConstructor
public class TipoTramoController {
    private final TipoTramoService tipoTramoService;

    @GetMapping // GET /api/v1/rutas/tipo-tramos
    public ResponseEntity<List<TipoTramo>> obtenerTodos() {
        return ResponseEntity.ok(tipoTramoService.obtenerTodosLosTiposTramo());
    }

    @GetMapping("/{idTipoTramo}") // GET /api/v1/rutas/tipo-tramos/{id}
    public ResponseEntity<TipoTramo> obtenerPorId(@PathVariable Integer idTipoTramo) {
        return ResponseEntity.ok(tipoTramoService.obtenerTipoTramoPorId(idTipoTramo));
    }
    //put
    @PutMapping("/{idTramo}")
    public ResponseEntity<TipoTramo> actualizarTipoTramo(@PathVariable Integer id, @RequestBody TipoTramo tipoTramo) {
        return ResponseEntity.ok(tipoTramoService.actualizarTipoTramo(id, tipoTramo));
    }

    //post
    @PostMapping
    public ResponseEntity<TipoTramo> crearTramo(@RequestBody TipoTramo tipoTramo) {
        return ResponseEntity.ok(tipoTramoService.crearTipoTramo(tipoTramo));
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoTramo(@PathVariable Integer id) {
        tipoTramoService.eliminarTipoTramo(id);
        return ResponseEntity.noContent().build();
    }
}
