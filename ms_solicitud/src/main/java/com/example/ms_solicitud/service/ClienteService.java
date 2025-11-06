package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Cliente;
import com.example.ms_solicitud.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    @Transactional
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente obtenerClientePorDni(Integer dni) {
        return clienteRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));
    }

    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsById(cliente.getDni())) {
            throw new RuntimeException("Ya existe un cliente con el DNI: " + cliente.getDni());
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente actualizarCliente(Integer dni, Cliente clienteActualizado) {

        Cliente clienteExistente = obtenerClientePorDni(dni);

        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setMail(clienteActualizado.getMail());

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    public void eliminarCliente(Integer dni) {
        if (!clienteRepository.existsById(dni)) {
            throw new RuntimeException("No se puede eliminar. Cliente no encontrado con DNI: " + dni);
        }
        clienteRepository.deleteById(dni);
    }

}

