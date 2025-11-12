package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camion;
import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.dto.CapacidadRequest;
import com.example.ms_rutas.model.dto.CapacidadResponse;
import com.example.ms_rutas.model.dto.ConsumoBaseResponse;
import com.example.ms_rutas.model.dto.CostoTrasladoResponse;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ms_rutas.repository.CamionRepository;


import java.util.List;

@Service
public class CamionService {
    private final CamionRepository camionRepository;
    public CamionService(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }

    @Transactional(readOnly = true)
    public List<Camion> obtenerTodasLosCamiones() {
        return camionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Camion obtenerCamionPorPatente(String patente) {
        return camionRepository.findById(patente)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con ID: " + patente));
    }

    @Transactional
    public Camion crearCamion(Camion camion) {
        return camionRepository.save(camion);
    }

    @Transactional
    public Camion actualizarCamion(String patente, Camion camionActualizada) {
        Camion camionExistente = obtenerCamionPorPatente(patente);
        return camionRepository.save(camionExistente);
    }

    @Transactional
    public void eliminarCamion(String patente) {
        if (!camionRepository.existsById(patente)) {
            throw new RuntimeException("No se puede eliminar. Camion no encontrado con patente: " + patente);
        }
        camionRepository.deleteById(patente);
    }
    @Transactional
    public CapacidadResponse consultarCapacidadCamion(String patente, CapacidadRequest capacidadRequest){
        Camion camion = camionRepository.findById(patente)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con ID: " + patente));
        CapacidadResponse capacidadResponse = new CapacidadResponse();
        capacidadResponse.setPatente(camion.getPatente());
        if (camion.getCapacidadPeso() >= capacidadRequest.getPesoContenedor() && camion.getCapacidadVolumen() >= capacidadRequest.getVolumenContenedor()){
            capacidadResponse.setCumpleCapacidad(Boolean.TRUE);
        }else {
            capacidadResponse.setCumpleCapacidad(Boolean.FALSE);
        }
        return capacidadResponse;
    }
    @Transactional
    public List<Camion> obtenerCamionesAptos(CapacidadRequest capacidadRequest){
        Double pesoContenedor = capacidadRequest.getPesoContenedor();
        Double volumenContenedor = capacidadRequest.getVolumenContenedor();
        List<Camion> camionesAptos = camionRepository.encontrarDisponibles(pesoContenedor, volumenContenedor);
        if(camionesAptos.isEmpty()){
            throw new RuntimeException("Camion aptos no encontrados");}
        else{
            return camionesAptos;
            }
    }
    @Transactional
    public CostoTrasladoResponse obtenerCostoBaseCamion(String patente){
         Camion camion= camionRepository.findById(patente)
                 .orElseThrow(() -> new RuntimeException("Camion no encontrado con ID: " + patente));
         CostoTrasladoResponse costoTrasladoResponse = new CostoTrasladoResponse();
         costoTrasladoResponse.setPatente(camion.getPatente());
         costoTrasladoResponse.setCostoBaseTraslado(camion.getCostoBaseTraslado());
         return costoTrasladoResponse;

    }

    @Transactional
    public ConsumoBaseResponse obtenerConsumoBaseCamion(String patente){
        Camion camion = camionRepository.findById(patente)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con ID: " + patente));
        ConsumoBaseResponse consumoBaseResponse = new ConsumoBaseResponse();
        consumoBaseResponse.setPatente(camion.getPatente());
        consumoBaseResponse.setConsumoBase(camion.getConsCombKm());
        return consumoBaseResponse;
    }

}
