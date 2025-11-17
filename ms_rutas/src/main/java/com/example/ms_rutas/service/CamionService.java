package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camion;
import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.dto.CapacidadRequest;
import com.example.ms_rutas.model.dto.CapacidadResponse;
import com.example.ms_rutas.model.dto.ConsumoBaseResponse;
import com.example.ms_rutas.model.dto.CostoTrasladoResponse;
import com.example.ms_rutas.repository.CamioneroRepository;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ms_rutas.repository.CamionRepository;


import java.util.List;
@RequiredArgsConstructor
@Service
public class CamionService {
    private final CamionRepository camionRepository;
    private final CamioneroRepository camioneroRepository;


    @Transactional(readOnly = true)
    public List<Camion> obtenerTodasLosCamiones() {
        return camionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Camion obtenerCamionPorPatente(String patente) {
        return camionRepository.findById(patente)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado con patente: " + patente));
    }

    @Transactional
    public Camion crearCamion(Camion camion) {
        Integer cedula = camion.getCamionero().getCedulaCamionero();

        Camionero camioneroExistente = camioneroRepository.findById(cedula)
                .orElseThrow(() -> new RuntimeException("Camionero no encontrado con cédula: " + cedula));
        if (obtenerCamionPorPatente(camion.getPatente()) != null) {
            throw new RuntimeException("Camión ya existe");
        }

        // 3. Reemplazar el objeto Camionero parcial por el objeto gestionado
        camion.setCamionero(camioneroExistente);
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
            throw new RuntimeException("No se puede eliminar. Camión no encontrado con patente: " + patente);
        }
        camionRepository.deleteById(patente);
    }
    @Transactional
    public CapacidadResponse consultarCapacidadCamion(String patente, CapacidadRequest capacidadRequest){
        Camion camion = camionRepository.findById(patente)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado con patente: " + patente));
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
            throw new RuntimeException("Camiones aptos no encontrados");}
        else{
            return camionesAptos;
            }
    }
    @Transactional
    public CostoTrasladoResponse obtenerCostoBaseCamion(String patente){
         Camion camion= camionRepository.findById(patente)
                 .orElseThrow(() -> new RuntimeException("Camión no encontrado con patente: " + patente));
         CostoTrasladoResponse costoTrasladoResponse = new CostoTrasladoResponse();
         costoTrasladoResponse.setPatente(camion.getPatente());
         costoTrasladoResponse.setCostoBaseTraslado(camion.getCostoBaseTraslado());
         return costoTrasladoResponse;

    }

    @Transactional
    public ConsumoBaseResponse obtenerConsumoBaseCamion(String patente){
        Camion camion = camionRepository.findById(patente)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado con patente: " + patente));
        ConsumoBaseResponse consumoBaseResponse = new ConsumoBaseResponse();
        consumoBaseResponse.setPatente(camion.getPatente());
        consumoBaseResponse.setConsumoBase(camion.getConsCombKm());
        return consumoBaseResponse;
    }

}
