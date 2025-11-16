package com.example.ms_solicitud.controller;

import com.example.ms_solicitud.model.Contenedor;
import com.example.ms_solicitud.service.ContenedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/v1/solicitudes/contenedores")
    public class ContenedorController {

        private final ContenedorService contenedorService;

        public ContenedorController(ContenedorService contenedorService) {
            this.contenedorService = contenedorService;
        }

//        @GetMapping
//        public ResponseEntity<List<Contenedor>> obtenerTodos() {
//            return ResponseEntity.ok(contenedorService.);
//        }
        //get
        @GetMapping("/{idContenedor}")
        public ResponseEntity<Contenedor> obtenerPorId(@PathVariable Integer idContenedor) {
            return ResponseEntity.ok(contenedorService.obtenerContenedorPorId(idContenedor));
        }
        @GetMapping("/estado/{idContenedor}")
        public String consultarEstado(@PathVariable Integer idContenedor){
            return contenedorService.obtenerEstado(idContenedor);
        }
        @GetMapping("/pendientes")
        public List<Contenedor> obtenerPendientes(){
            return contenedorService.obtenerPendientes();
        }
        //post
        @PostMapping
        public ResponseEntity<Contenedor> crearContenedor(@RequestBody Contenedor contenedor) {
            Contenedor nuevo = contenedorService.crearContenedor(contenedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        }
        //put
        @PutMapping("/{id}")
        public ResponseEntity<Contenedor> actualizarContenedor(@PathVariable Integer id, @RequestBody Contenedor contenedor) {
            Contenedor actualizado = contenedorService.actualizarContenedor(id, contenedor);
            return ResponseEntity.ok(actualizado);
        }
        //delete
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarContenedor(@PathVariable Integer id) {
            contenedorService.eliminarContenedor(id);
            return ResponseEntity.noContent().build();
        }
}
