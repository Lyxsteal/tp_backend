package com.example.ms_solicitud.controller;

import com.example.ms_solicitud.model.Cliente;

import com.example.ms_solicitud.service.ClienteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService clienteService) { this.service = clienteService;
    }

    public ResponseEntity<Cliente> crearCliente (@RequestBody Cliente cliente){
        service.crearCliente(cliente);
        return ResponseEntity.ok(cliente);
    }
}
