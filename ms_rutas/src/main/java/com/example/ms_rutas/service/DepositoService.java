package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.Deposito;
import com.example.ms_rutas.model.dto.DepositoDto;
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
    public Deposito crearDeposito(DepositoDto depositoDto) {
        Deposito deposito = new Deposito();
        deposito.setCoordenadas(depositoDto.getCoordenadas());
        deposito.setDireccion(depositoDto.getDireccion());
        deposito.setNombre(depositoDto.getNombre());
        log.info("Deposito creado con exito");
        return depositoRepository.save(deposito);
    }

        @Transactional
        public Deposito actualizarDeposito(Integer id, Deposito depositoActualizado) {
            Deposito depositoExistente = obtenerDepositoPorId(id);
            depositoExistente.setDireccion(depositoActualizado.getDireccion());
            depositoExistente.setNombre(depositoActualizado.getNombre());
            depositoExistente.setCoordenadas(depositoActualizado.getCoordenadas());

            log.info("Actualizando deposito");
            return depositoRepository.save(depositoExistente);
        }

        @Transactional
        public void eliminarDeposito(Integer id) {
            if (!depositoRepository.existsById(id)) {
                log.warn("No se puedo eliminar deposito.No fue encontrado");
                throw new RuntimeException("No se puede eliminar. Depósito no encontrado con id: " + id);
            }
            log.info("Eliminando deposito con ID: " + id);
            depositoRepository.deleteById(id);
        }
    }
