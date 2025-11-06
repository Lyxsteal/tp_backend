package com.example.ms_solicitud.service;

import com.example.ms_solicitud.model.Tarifa;
import com.example.ms_solicitud.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarifaService {
    private final TarifaRepository tarifaRepository;
    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    @Transactional(readOnly = true)
    public List<Tarifa> obtenerTodasLasTarifas() {
        return tarifaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tarifa obtenerTarifaPorId(Integer id) {
        return tarifaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));
    }

    @Transactional
    public Tarifa crearTarifa(Tarifa tarifa) {
        // Aquí podrías agregar lógica de validación
        return tarifaRepository.save(tarifa);
    }

    @Transactional
    public Tarifa actualizarTarifa(Integer id, Tarifa tarifaActualizada) {
        Tarifa tarifaExistente = obtenerTarifaPorId(id);

        tarifaExistente.setValorFijoTramo(tarifaActualizada.getValorFijoTramo());
        tarifaExistente.setValorPorVolumen(tarifaActualizada.getValorPorVolumen());
        tarifaExistente.setValorFijoCombustible(tarifaActualizada.getValorFijoCombustible());

        return tarifaRepository.save(tarifaExistente);
    }

    @Transactional
    public void eliminarTarifa(Integer id) {
        if (!tarifaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Tarifa no encontrada con ID: " + id);
        }
        tarifaRepository.deleteById(id);
    }
}
