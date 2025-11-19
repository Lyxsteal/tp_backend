package com.example.ms_solicitud.controller;

import com.example.ms_solicitud.model.Cliente;

import com.example.ms_solicitud.model.Contenedor;
import com.example.ms_solicitud.service.ClienteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/solicitudes/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService clienteService) { this.service = clienteService;
    }
    @GetMapping()
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes(){
        return ResponseEntity.ok(service.obtenerTodosLosClientes());
    }
    //get
    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> obtenerClientePorDni(@PathVariable Integer dni) {
        return ResponseEntity.ok(service.obtenerClientePorDni(dni));
    }
    @PostMapping
    public ResponseEntity<Cliente> crearCliente (@RequestBody Cliente cliente){
        service.crearCliente(cliente);
        return ResponseEntity.ok(cliente);
    }
    @PutMapping("/{dni}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Integer dni, @RequestBody Cliente cliente) {
        Cliente actualizado = service.actualizarCliente(dni, cliente);
        return ResponseEntity.ok(actualizado);
    }
    //delete
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> eliminarContenedor(@PathVariable Integer dni) {
        service.eliminarCliente(dni);
        return ResponseEntity.noContent().build();
    }
}
