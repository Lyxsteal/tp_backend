package com.example.ms_solicitud.controller;

import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.service.TarifaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/solicitudes/tarifas")
@RequiredArgsConstructor
public class TarifaController {
    private final TarifaService service;
    //get
    @GetMapping
    public ResponseEntity<List<Tarifa>> obtenerTarifas() {
        return ResponseEntity.ok(service.obtenerTodasLasTarifas());
    }
    @GetMapping("/{isTarifa}")
    public  ResponseEntity<Tarifa> obtenerTarifaPorId(Integer idTarifa){
        return ResponseEntity.ok(service.obtenerTarifaPorId(idTarifa));
    }
    //post
    @PostMapping
    public ResponseEntity<Tarifa> crearTarifa (@RequestBody Tarifa tarifa){
        service.crearTarifa(tarifa);
        return ResponseEntity.ok(tarifa);
    }
    //put
    @PutMapping("/{idTarifa}")
    public ResponseEntity<Tarifa> actualizarTarifa(@PathVariable Integer idTarifa, @RequestBody Tarifa tarifa){
        return ResponseEntity.ok(service.actualizarTarifa(idTarifa, tarifa));
    }
    //delete
    @DeleteMapping("/{idTarifa}")
    public ResponseEntity<Void> borrarTarifa(@PathVariable Integer idTarifa){
        service.eliminarTarifa(idTarifa);
        return ResponseEntity.noContent().build();
    }
}
