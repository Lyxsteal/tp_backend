package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.Deposito;
import com.example.ms_rutas.repository.CamioneroRepository;
import com.example.ms_rutas.repository.DepositoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DepositoService {
    private final DepositoRepository depositoRepository;
    private static final Logger log = LoggerFactory.getLogger(CamionService.class);

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    @Transactional(readOnly = true)
        public List<Deposito> obtenerTodosLosDepositos() {
        log.info("Buscando todos los Depositos");
        return depositoRepository.findAll();
        }

        @Transactional(readOnly = true)
        public Deposito obtenerDepositoPorId(Integer id) {
            return depositoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontro Deposito");
                        return new RuntimeException("Deposito no encontrado con id: " + id);
                    });
        }

        @Transactional
        public Deposito crearDeposito(Deposito deposito) {
        if (depositoRepository.existsById(deposito.getId())) {
            log.warn("ya existe el contenedor");
            throw new RuntimeException("el contenedor ya existe");
        }
        return depositoRepository.save(deposito);

        }

        @Transactional
        public Deposito actualizarDeposito(Integer id, Deposito depositoActualizado) {
            Deposito depositoExistente = obtenerDepositoPorId(id);
            depositoExistente.setDireccion(depositoActualizado.getDireccion());
            depositoExistente.setNombre(depositoActualizado.getNombre());
            depositoExistente.setUbicacion(depositoActualizado.getUbicacion());

            log.info("actualizando deposito");
            return depositoRepository.save(depositoExistente);
        }

        @Transactional
        public void eliminarDeposito(Integer id) {
            if (!depositoRepository.existsById(id)) {
                log.warn("No se puedo eliminar deposito.No fue encontrado");
                throw new RuntimeException("No se puede eliminar. Depósito no encontrado con id: " + id);
            }
            log.info("eliminando deposito con ID: " + id);
            depositoRepository.deleteById(id);
        }
    }
