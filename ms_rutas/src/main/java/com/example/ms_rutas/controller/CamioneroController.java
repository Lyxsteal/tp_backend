package com.example.ms_rutas.controller;


import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.service.CamioneroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rutas/camioneros")
@RequiredArgsConstructor
public class CamioneroController {
    private final CamioneroService camioneroService;

    //get
    @GetMapping("/{cedula}")
    public ResponseEntity<Camionero> obtenerCamionero(@PathVariable Integer cedula) {
        return ResponseEntity.ok(camioneroService.obtenerCamioneroPorCedula(cedula));
    }


    //post
    @PostMapping
    public ResponseEntity<Camionero> crearCamionero(@RequestBody Camionero camionero) {
        return ResponseEntity.ok(camioneroService.crearCamionero(camionero));
    }

    //put
    @PutMapping
    public ResponseEntity<Camionero> actualizarCamionero(@PathVariable Integer cedula, @RequestBody Camionero camionero) {
        return ResponseEntity.ok(camioneroService.actualizarCamionero(cedula, camionero));
    }

    //delete
    @DeleteMapping
    public ResponseEntity<Void> eliminarCamionero(@PathVariable Integer cedula) {
        camioneroService.eliminarCamionero(cedula);
        return ResponseEntity.noContent().build();
    }
}
