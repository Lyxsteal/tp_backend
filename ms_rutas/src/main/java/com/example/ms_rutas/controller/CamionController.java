package com.example.ms_rutas.controller;

import com.example.ms_rutas.model.Camion;
import com.example.ms_rutas.model.Deposito;
import com.example.ms_rutas.model.dto.CapacidadRequest;
import com.example.ms_rutas.model.dto.CapacidadResponse;
import com.example.ms_rutas.model.dto.ConsumoBaseResponse;
import com.example.ms_rutas.model.dto.CostoTrasladoResponse;
import com.example.ms_rutas.service.CamionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/rutas/camiones")
@RequiredArgsConstructor
public class CamionController {
    private final CamionService camionService;

    @GetMapping()
    public List<Camion> obtenerTodosLosCamiones(){
        return camionService.obtenerTodasLosCamiones();
    }
    //get
    @GetMapping("/{camion_patente}")
    public ResponseEntity<Camion>obtenerCamionPorPatente(@PathVariable String camion_patente) {
        return ResponseEntity.ok(camionService.obtenerCamionPorPatente(camion_patente));
    }

    @GetMapping("/capacidad-maxima/{patente}")
    public CapacidadResponse consultarCapacidadMaxima(@PathVariable String patente, @RequestParam Double pesoContenedor, @RequestParam Double volumenContenedor) {

        CapacidadRequest capacidadRequest = new CapacidadRequest();
        capacidadRequest.setPesoContenedor(pesoContenedor);
        capacidadRequest.setVolumenContenedor(volumenContenedor);

        return camionService.consultarCapacidadCamion(patente, capacidadRequest);
    }

    //get
    @GetMapping("/camiones-aptos")
    public ResponseEntity<List<Camion>>ObtenerCamionesAptos(@RequestParam Double pesoContenedor, @RequestParam Double volumenContenedor) {
        CapacidadRequest capacidadRequest = new CapacidadRequest();
        capacidadRequest.setPesoContenedor(pesoContenedor);
        capacidadRequest.setVolumenContenedor(volumenContenedor);
        return ResponseEntity.ok(camionService.obtenerCamionesAptos(capacidadRequest));
    }
    //get
    @GetMapping("/costo-base/{patente}")
    public CostoTrasladoResponse ObtenerCostoBaseDelCamion(@PathVariable String patente) {
        return camionService.obtenerCostoBaseCamion(patente);
    }
    //get
    @GetMapping("/consumo-prom/{patente}")
    public ConsumoBaseResponse ObtenerConsumoBase(@PathVariable String patente) {
        return camionService.obtenerConsumoPromedioCamion(patente);
    }
    //put
    @PutMapping("/{camion_patente}")
    public ResponseEntity<Camion> actualizarCamion(@PathVariable String camion_patente, @RequestBody Camion camion){
        return ResponseEntity.ok(camionService.actualizarCamion(camion_patente, camion));
    }
    //post
    @PostMapping
    public ResponseEntity<Camion> crearCamion(@RequestBody Camion camion){
        return ResponseEntity.ok(camionService.crearCamion(camion));
    } //revisar tema patente, deberia pasarle patente

    //delete
    @DeleteMapping("/{camion_patente}")
    public ResponseEntity<Void> eliminarCamion(@PathVariable String camion_patente){
        camionService.eliminarCamion(camion_patente);
        return ResponseEntity.noContent().build();
    }
}

