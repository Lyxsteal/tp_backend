package com.example.ms_solicitud.controller;

import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.service.TarifaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/solicitudes/tarifas")
@RequiredArgsConstructor
public class TarifaController {
    private final TarifaService service;
    //get
    //post
    @PostMapping
    public ResponseEntity<Tarifa> crearTarifa (@RequestBody Tarifa tarifa){
        service.crearTarifa(tarifa);
        return ResponseEntity.ok(tarifa);
    }
    //put
}
