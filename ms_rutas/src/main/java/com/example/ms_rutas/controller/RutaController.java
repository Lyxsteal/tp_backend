package com.example.ms_rutas.controller;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/rutas")
@RequiredArgsConstructor
public class RutaController {
    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }
    //get
    //put
    //post
    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@RequestBody Ruta ruta){
        return ResponseEntity.ok(rutaService.crearRuta(ruta));
    }
    //delete
}
