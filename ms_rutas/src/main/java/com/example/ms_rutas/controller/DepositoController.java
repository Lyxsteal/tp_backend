package com.example.ms_rutas.controller;


import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.Deposito;
import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.service.DepositoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rutas/depositos")
@RequiredArgsConstructor
public class DepositoController {
    private final DepositoService depositoService;
    //get
    @GetMapping()
    public List<Deposito> obtenerTodosLosDepositos(){
        return depositoService.obtenerTodosLosDepositos();
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<Deposito> obtenerDeposito(@PathVariable Integer idDeposito) {
        return ResponseEntity.ok(depositoService.obtenerDepositoPorId(idDeposito));
    }
    //put
    @PutMapping("/{idDeposito}")
    public ResponseEntity<Deposito> actualizardeposito(@PathVariable Integer idDeposito , @RequestBody Deposito deposito) {
        return ResponseEntity.ok(depositoService.actualizarDeposito(idDeposito, deposito));
    }
    //post
    @PostMapping()
    public ResponseEntity<Deposito> crearDeposito(@RequestBody Deposito deposito){
        return ResponseEntity.ok(depositoService.crearDeposito(deposito));
    }

    //delete
    @DeleteMapping("/{idDeposito}")
    public ResponseEntity<Void> eliminarDesposito(@PathVariable Integer idDeposito) {
            depositoService.eliminarDeposito(idDeposito);
       return ResponseEntity.noContent().build();
    }
}
