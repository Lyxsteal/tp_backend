package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Cliente;
import com.example.ms_solicitud.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    @Transactional
    public List<Cliente> obtenerTodosLosClientes() {
        log.info("Buscando todos los clientes");
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente obtenerClientePorDni(Integer dni) {
        log.info("Obteniendo cliente con DNI: " + dni);
        return clienteRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));
    }

    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsById(cliente.getDni())) {
            log.error("Ya existe el cliente con dni" + cliente.getDni());
            throw new RuntimeException("Ya existe un cliente con el DNI: " + cliente.getDni());
        }
        log.info("guardando cliente");
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente actualizarCliente(Integer dni, Cliente clienteActualizado) {

        Cliente clienteExistente = obtenerClientePorDni(dni);
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setMail(clienteActualizado.getMail());
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        log.info("Actualizando cliente con DNI: " + dni);
        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    public void eliminarCliente(Integer dni) {
        if (!clienteRepository.existsById(dni)) {
            log.error("No se pudo encontrar el Cliente con DNI:" + dni);
            throw new RuntimeException("No se puede eliminar. Cliente no encontrado con DNI: " + dni);
        }
        log.info("Cliente con DNI " + dni + "eliminado con éxito.");
        clienteRepository.deleteById(dni);
    }

}

