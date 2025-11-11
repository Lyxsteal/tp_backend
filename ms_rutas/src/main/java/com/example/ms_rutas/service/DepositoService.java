package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.Deposito;
import com.example.ms_rutas.repository.CamioneroRepository;
import com.example.ms_rutas.repository.DepositoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepositoService {
    private final DepositoRepository depositoRepository;

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    @Transactional(readOnly = true)
        public List<Deposito> obtenerTodosLosDepositos() {
            return depositoRepository.findAll();
        }

        @Transactional(readOnly = true)
        public Deposito obtenerDepositoPorId(Integer id) {
            return depositoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Deposito no encontrado con id: " + id));
        }

        @Transactional
        public Deposito crearDeposito(Deposito deposito) {
            return depositoRepository.save(deposito);
        }

        @Transactional
        public Deposito actualizarDeposito(Integer id, Deposito depositoActualizado) {
            Deposito depositoExistente = obtenerDepositoPorId(id);

            return depositoRepository.save(depositoExistente);
        }

        @Transactional
        public void eliminarDeposito(Integer id) {
            if (!depositoRepository.existsById(id)) {
                throw new RuntimeException("No se puede eliminar. Depósito no encontrado con id: " + id);
            }
            depositoRepository.deleteById(id);
        }
    }
